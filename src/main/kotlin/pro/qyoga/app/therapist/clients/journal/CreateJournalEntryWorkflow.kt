package pro.qyoga.app.therapist.clients.journal

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalsService
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.platform.spring.sdj.ref

sealed interface CreateJournalEntryResult {
    data class Success(val entry: JournalEntry) : CreateJournalEntryResult
    data object ClientNotFound : CreateJournalEntryResult
}

@Component
class CreateJournalEntryWorkflow(
    private val clientsService: ClientsService,
    private val journalsService: JournalsService,
    private val therapeuticTasksService: TherapeuticTasksService
) {

    @Transactional
    fun createJournalEntry(
        clientId: Long,
        createJournalEntryRequest: CreateJournalEntryRequest,
        principal: QyogaUserDetails,
    ): CreateJournalEntryResult {
        val client = clientsService.findClient(clientId)
            ?: return CreateJournalEntryResult.ClientNotFound

        val therapeuticTask = therapeuticTasksService.createTherapeuticTask(
            principal.id,
            createJournalEntryRequest.therapeuticTaskName
        )
        val newEntry = JournalEntry(
            client.ref(),
            createJournalEntryRequest.date,
            therapeuticTask.ref(),
            createJournalEntryRequest.journalEntryText
        )
        val entry = journalsService.createJournalEntry(newEntry)

        return CreateJournalEntryResult.Success(entry)
    }

}