package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.clients.journal.CreateJournalEntryRequest
import pro.qyoga.app.therapist.clients.journal.CreateJournalEntryResult
import pro.qyoga.app.therapist.clients.journal.CreateJournalEntryWorkflow
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.hydrate
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.tests.fixture.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.data.randomRecentLocalDate


@Component
class ClientJournalBackgrounds(
    private val createJournalEntryWorkflow: CreateJournalEntryWorkflow,
    private val therapeuticTasksService: TherapeuticTasksService
) {

    fun createJournalEntry(
        clientId: Long,
        createJournalEntryRequest: CreateJournalEntryRequest,
        therapist: QyogaUserDetails
    ): JournalEntry {
        val res = createJournalEntryWorkflow.createJournalEntry(clientId, createJournalEntryRequest, therapist)
        return (res as CreateJournalEntryResult.Success).entry
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
        journalEntries.hydrate(therapeuticTasksService::findAllById)

}