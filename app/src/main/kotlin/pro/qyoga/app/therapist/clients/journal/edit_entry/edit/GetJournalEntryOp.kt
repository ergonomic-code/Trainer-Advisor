package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.ClientNotFound
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.model.JournalEntry
import java.util.*

@Component
class GetJournalEntryOp(
    private val clientsRepo: ClientsRepo,
    private val journalEntriesRepo: JournalEntriesRepo
) {

    fun getJournalEntry(clientId: UUID, entryId: Long): JournalEntry? {
        if (!clientsRepo.existsById(clientId)) {
            throw ClientNotFound(clientId)
        }

        return journalEntriesRepo.getEntry(clientId, entryId, fetch = JournalEntry.Fetch.summaryRefs)
    }

}