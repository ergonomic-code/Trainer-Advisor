package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.ClientNotFound
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.EntryNotFound
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalsService
import pro.qyoga.core.clients.journals.api.hydrate
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService

@Component
class GetJournalEntryWorkflow(
    private val clientsService: ClientsService,
    private val journalsService: JournalsService,
    private val therapeuticTasksService: TherapeuticTasksService
) {

    fun getJournalEntry(clientId: Long, entryId: Long): JournalEntry {
        if (!clientsService.clientExists(clientId)) {
            throw ClientNotFound(clientId)
        }

        return journalsService.getJournalEntry(clientId, entryId)
            ?.hydrate(therapeuticTasksService::findAllById)
            ?: throw EntryNotFound(clientId, entryId)
    }

}