package pro.qyoga.app.therapist.clients.journal.edit_entry.create

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.ClientNotFound
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails

@Component
class CreateJournalEntryOp(
    private val clientsRepo: ClientsRepo,
    private val journalEntriesRepo: JournalEntriesRepo,
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @Transactional
    fun createJournalEntry(
        clientId: Long,
        editJournalEntryRequest: EditJournalEntryRequest,
        principal: QyogaUserDetails,
    ): JournalEntry {
        val client = clientsRepo.findByIdOrNull(clientId)
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
        val persistedEntry = journalEntriesRepo.save(newEntry)

        return persistedEntry
    }

}