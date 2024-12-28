package pro.qyoga.core.clients.journals

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.clients.journals.dtos.JournalPageRequest
import pro.qyoga.core.clients.journals.errors.DuplicatedDate
import pro.qyoga.core.clients.journals.model.JournalEntry
import kotlin.reflect.KProperty1


@Repository
class JournalEntriesRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : ErgoRepository<JournalEntry, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    BasicPersistentEntity(TypeInformation.of(JournalEntry::class.java)),
    jdbcConverter,
    relationalMappingContext
) {

    @Transactional
    override fun <S : JournalEntry?> save(instance: S & Any): S & Any {
        return saveAndMapDuplicatedKeyException(instance) { ex ->
            DuplicatedDate(instance, ex)
        }
    }

    fun getJournalPage(journalPageRequest: JournalPageRequest): Page<JournalEntry> {
        return findPage(
            pageRequest = PageRequest.of(0, journalPageRequest.pageSize, sortBy(JournalEntry::date).descending()),
            fetch = journalPageRequest.fetch,
        ) {
            JournalEntry::client isEqual AggregateReference.to(journalPageRequest.clientId)
            JournalEntry::date isLessThanIfNotNull journalPageRequest.date
        }
    }

    fun getEntry(
        clientId: Long,
        entryId: Long,
        fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()
    ): JournalEntry? {
        return findOne(fetch) {
            JournalEntry::client isEqual AggregateReference.to(clientId)
            JournalEntry::id isEqual entryId
        }
    }

}