package pro.qyoga.core.clients.journals.internal

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalPageRequest
import pro.qyoga.platform.spring.sdj.query
import pro.qyoga.platform.spring.sdj.sortBy


@Repository
class JournalEntriesRepo(
    private val jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<JournalEntry, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(JournalEntry::class.java)),
    jdbcConverter
) {

    fun getJournalPage(journalPageRequest: JournalPageRequest): Page<JournalEntry> {
        val query = query {
            JournalEntry::client isEqual AggregateReference.to(journalPageRequest.clientId)
            JournalEntry::date isLessThanIfNotNull journalPageRequest.date
        }

        return jdbcAggregateTemplate.findAll(
            query,
            JournalEntry::class.java,
            PageRequest.of(0, journalPageRequest.pageSize, sortBy(JournalEntry::date).descending())
        )
    }

}