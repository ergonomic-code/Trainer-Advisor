package pro.qyoga.core.clients.journals.api

import org.springframework.data.domain.Page


interface JournalsService {

    fun createJournalEntry(newEntry: JournalEntry): JournalEntry

    fun getJournalPage(journalPageRequest: JournalPageRequest): Page<JournalEntry>

    fun getJournalEntry(clientId: Long, entryId: Long): JournalEntry?

    fun deleteEntry(clientId: Long, entryId: Long)

}