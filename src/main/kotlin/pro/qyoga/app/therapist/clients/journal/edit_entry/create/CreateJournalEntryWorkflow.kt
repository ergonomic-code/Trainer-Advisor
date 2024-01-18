package pro.qyoga.app.therapist.clients.journal.edit_entry.create

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.ClientNotFound
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.core.clients.journals.api.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalsService
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.platform.spring.sdj.erpo.hydration.ref

@Component
class CreateJournalEntryWorkflow(
    private val clientsService: ClientsService,
    private val journalsService: JournalsService,
    private val therapeuticTasksService: TherapeuticTasksService
) {

    @Transactional
    fun createJournalEntry(
        clientId: Long,
        editJournalEntryRequest: EditJournalEntryRequest,
        principal: QyogaUserDetails,
    ): JournalEntry {
        val client = clientsService.findClient(clientId)
            ?: throw ClientNotFound(clientId)

        val therapeuticTask = therapeuticTasksService.getOrCreate(
            principal.id,
            editJournalEntryRequest.therapeuticTaskName
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