package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.clients.journals.api.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.api.EntryNotFound
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalsService
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService
import pro.qyoga.core.users.internal.QyogaUserDetails

@Component
class EditJournalEntryWorkflow(
    private val journalsService: JournalsService,
    private val therapeuticTasksService: TherapeuticTasksService
) {

    @Transactional
    fun editJournalEntry(
        clientId: Long,
        entryId: Long,
        editJournalEntryRequest: EditJournalEntryRequest,
        principal: QyogaUserDetails,
    ): JournalEntry {
        var entry = journalsService.getJournalEntry(clientId, entryId)
            ?: throw EntryNotFound(clientId, entryId)

        val therapeuticTask = therapeuticTasksService.getOrCreate(
            principal.id,
            editJournalEntryRequest.therapeuticTaskName
        )

        entry = entry.updateBy(editJournalEntryRequest, therapeuticTask)
        val persistedEntry = journalsService.createJournalEntry(entry)

        return persistedEntry
    }

}