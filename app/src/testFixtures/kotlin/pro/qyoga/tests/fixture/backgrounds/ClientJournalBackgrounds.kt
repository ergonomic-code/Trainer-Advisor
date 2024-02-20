package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Page
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.FetchSpec
import pro.azhidkov.platform.spring.sdj.erpo.hydration.hydrate
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryWorkflow
import pro.qyoga.core.clients.journals.JournalsService
import pro.qyoga.core.clients.journals.dtos.JournalPageRequest
import pro.qyoga.core.clients.journals.model.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.tests.fixture.data.randomRecentLocalDate
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother


@Component
class ClientJournalBackgrounds(
    private val createJournalEntryWorkflow: CreateJournalEntryWorkflow,
    private val journalsService: JournalsService,
    private val jdbcAggregateOperations: JdbcAggregateOperations
) {

    fun createJournalEntry(
        clientId: Long,
        editJournalEntryRequest: EditJournalEntryRequest,
        therapist: QyogaUserDetails
    ): JournalEntry {
        return createJournalEntryWorkflow.createJournalEntry(clientId, editJournalEntryRequest, therapist)
    }

    fun createEntries(clientId: Long, therapist: QyogaUserDetails, count: Int): List<JournalEntry> {
        val uniqueDates = generateSequence { randomRecentLocalDate() }
            .distinct()
            .asIterable()
        return (1..count).zip(uniqueDates).map { (_, date) ->
            createJournalEntry(clientId, JournalEntriesObjectMother.journalEntry(date = date), therapist)
        }
    }

    fun hydrate(journalEntries: List<JournalEntry>): List<JournalEntry> =
        jdbcAggregateOperations.hydrate(
            journalEntries,
            FetchSpec(JournalEntry::therapeuticTask)
        )

    fun getWholeJournal(clientId: Long): Page<JournalEntry> {
        return journalsService.getJournalPage(
            JournalPageRequest.wholeJournal(
                clientId,
                fetch = listOf(JournalEntry::therapeuticTask)
            )
        )
    }

}