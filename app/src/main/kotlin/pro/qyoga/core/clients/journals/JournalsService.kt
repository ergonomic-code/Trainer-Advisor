package pro.qyoga.core.clients.journals

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import pro.qyoga.core.clients.journals.dtos.JournalPageRequest
import pro.qyoga.core.clients.journals.internal.JournalEntriesRepo
import pro.qyoga.core.clients.journals.model.JournalEntry

@Service
class JournalsService(
    private val journalEntriesRepo: JournalEntriesRepo
) {

    fun createJournalEntry(newEntry: JournalEntry): JournalEntry {
        return journalEntriesRepo.save(newEntry)
    }

    fun getJournalPage(journalPageRequest: JournalPageRequest): Page<JournalEntry> {
        return journalEntriesRepo.getJournalPage(journalPageRequest)
    }

    fun getJournalEntry(clientId: Long, entryId: Long): JournalEntry? {
        return journalEntriesRepo.getEntry(clientId, entryId, fetch = listOf(JournalEntry::therapeuticTask))
    }

    fun deleteEntry(clientId: Long, entryId: Long) {
        journalEntriesRepo.deleteById(entryId)
    }

}