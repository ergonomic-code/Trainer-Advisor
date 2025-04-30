package pro.qyoga.core.clients.journals

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.clients.journals.dtos.JournalPageRq
import pro.qyoga.core.clients.journals.errors.DuplicatedDate
import pro.qyoga.core.clients.journals.model.JournalEntry
import java.util.*
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
    JournalEntry::class,
    jdbcConverter,
    relationalMappingContext
) {

    @Transactional
    override fun <S : JournalEntry?> save(instance: S & Any): S & Any {
        return saveAndMapDuplicatedKeyException(instance) { ex ->
            DuplicatedDate(instance, ex)
        }
    }

    fun getJournalPage(journalPageRq: JournalPageRq): Slice<JournalEntry> {
        return findSlice(
            pageRequest = PageRequest.of(0, journalPageRq.pageSize, sortBy(JournalEntry::date).descending()),
            fetch = journalPageRq.fetch,
        ) {
            JournalEntry::clientRef isEqual AggregateReference.to(journalPageRq.clientId)
            JournalEntry::date isLessThanIfNotNull journalPageRq.date
        }
    }

    fun getEntry(
        clientId: UUID,
        entryId: Long,
        fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()
    ): JournalEntry? {
        return findOne(fetch) {
            JournalEntry::clientRef isEqual AggregateReference.to(clientId)
            JournalEntry::id isEqual entryId
        }
    }

}