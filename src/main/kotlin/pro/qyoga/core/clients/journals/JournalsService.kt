package pro.qyoga.core.clients.journals

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Page
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.stereotype.Service
import pro.qyoga.core.clients.journals.dtos.JournalPageRequest
import pro.qyoga.core.clients.journals.errors.DuplicatedDate
import pro.qyoga.core.clients.journals.errors.EntryNotFound
import pro.qyoga.core.clients.journals.internal.JournalEntriesRepo
import pro.qyoga.core.clients.journals.model.JournalEntry

@Service
class JournalsService(
    private val journalEntriesRepo: JournalEntriesRepo
) {

    fun createJournalEntry(newEntry: JournalEntry): JournalEntry {
        val result = runCatching { journalEntriesRepo.save(newEntry) }
        val ex = result.exceptionOrNull()
        if ((ex as? DbActionExecutionException)?.cause is DuplicateKeyException) {
            throw DuplicatedDate(newEntry, ex.cause as DuplicateKeyException)
        }

        return result.getOrThrow()
    }

    fun getJournalPage(journalPageRequest: JournalPageRequest): Page<JournalEntry> {
        return journalEntriesRepo.getJournalPage(journalPageRequest)
    }

    fun getJournalEntry(clientId: Long, entryId: Long): JournalEntry? {
        return journalEntriesRepo.getEntry(clientId, entryId, fetch = listOf(JournalEntry::therapeuticTask))
    }

    fun deleteEntry(clientId: Long, entryId: Long) {
        if (!journalEntriesRepo.existsById(entryId)) {
            throw EntryNotFound(clientId, entryId)
        }
        journalEntriesRepo.deleteById(entryId)
    }

}