package pro.qyoga.app.therapist.clients.journal.edit_entry.create

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import java.util.*

@Component
class CreateJournalEntryOp(
    private val clientsRepo: ClientsRepo,
    private val journalEntriesRepo: JournalEntriesRepo,
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @Transactional
    fun createJournalEntry(
        clientId: UUID,
        editJournalEntryRq: EditJournalEntryRq,
        principal: QyogaUserDetails,
    ): JournalEntry {
        val client = clientsRepo.findByIdOrNull(clientId)
        checkNotNull(client) { "Client for journal entry not found by id=$clientId" }

        val therapeuticTask = therapeuticTasksRepo.getOrCreate(
            TherapeuticTask(principal.id, editJournalEntryRq.therapeuticTaskTitle)
        )
        val newEntry = JournalEntry(
            client.ref(),
            editJournalEntryRq.date,
            therapeuticTask.ref(),
            editJournalEntryRq.journalEntryText
        )
        val persistedEntry = journalEntriesRepo.save(newEntry)

        return persistedEntry
    }

}