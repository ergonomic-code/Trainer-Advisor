package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.ClientNotFound
import pro.qyoga.core.clients.cards.ClientsService
import pro.qyoga.core.clients.journals.JournalsService
import pro.qyoga.core.clients.journals.model.JournalEntry

@Component
class GetJournalEntryWorkflow(
    private val clientsService: ClientsService,
    private val journalsService: JournalsService
) {

    fun getJournalEntry(clientId: Long, entryId: Long): JournalEntry? {
        if (!clientsService.clientExists(clientId)) {
            throw ClientNotFound(clientId)
        }

        return journalsService.getJournalEntry(clientId, entryId)
    }

}