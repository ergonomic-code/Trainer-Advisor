package pro.qyoga.core.clients.journals.internal

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalPageRequest
import pro.qyoga.core.clients.journals.api.JournalsService


@Service
class JournalsServiceImpl(
    private val journalEntriesRepo: JournalEntriesRepo
) : JournalsService {

    override fun createJournalEntry(newEntry: JournalEntry): JournalEntry {
        return journalEntriesRepo.save(newEntry)
    }

    override fun getJournalPage(journalPageRequest: JournalPageRequest): Page<JournalEntry> {
        return journalEntriesRepo.getJournalPage(journalPageRequest)
    }

}