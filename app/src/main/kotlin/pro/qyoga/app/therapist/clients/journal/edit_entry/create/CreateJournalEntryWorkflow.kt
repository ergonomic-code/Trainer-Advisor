package pro.qyoga.app.therapist.clients.journal.edit_entry.create

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.ClientNotFound
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.core.clients.journals.JournalsService
import pro.qyoga.core.clients.journals.model.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails

@Component
class CreateJournalEntryWorkflow(
    private val clientsService: ClientsService,
    private val journalsService: JournalsService,
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @Transactional
    fun createJournalEntry(
        clientId: Long,
        editJournalEntryRequest: EditJournalEntryRequest,
        principal: QyogaUserDetails,
    ): JournalEntry {
        val client = clientsService.findClient(clientId)
            ?: throw ClientNotFound(clientId)

        val therapeuticTask = therapeuticTasksRepo.getOrCreate(
            TherapeuticTask(principal.id, editJournalEntryRequest.therapeuticTaskName)
        )
        val newEntry = JournalEntry(
            client.ref(),
            editJournalEntryRequest.date,
            therapeuticTask.ref(),
            editJournalEntryRequest.journalEntryText
        )
        val persistedEntry = journalsService.createJournalEntry(newEntry)

        return persistedEntry
    }

}