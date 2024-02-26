package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.clients.journals.JournalsService
import pro.qyoga.core.clients.journals.model.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails

@Component
class EditJournalEntryWorkflow(
    private val journalsService: JournalsService,
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @Transactional
    fun editJournalEntry(
        clientId: Long,
        entryId: Long,
        editJournalEntryRequest: EditJournalEntryRequest,
        principal: QyogaUserDetails,
    ): JournalEntry? {
        var entry = journalsService.getJournalEntry(clientId, entryId)
            ?: error("Entry $entryId for client $clientId not found")

        val therapeuticTask = therapeuticTasksRepo.getOrCreate(
            TherapeuticTask(principal.id, editJournalEntryRequest.therapeuticTaskName)
        )

        entry = entry.updateBy(editJournalEntryRequest, therapeuticTask)
        val persistedEntry = journalsService.createJournalEntry(entry)

        return persistedEntry
    }

}