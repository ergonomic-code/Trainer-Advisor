package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.query.query
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.clients.journals.model.updatedBy
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails

@Component
class EditJournalEntryOp(
    private val journalsRepo: JournalEntriesRepo,
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) : (ClientRef, Long, EditJournalEntryRq, QyogaUserDetails) -> JournalEntry? {

    @Transactional
    override operator fun invoke(
        clientId: ClientRef,
        entryId: Long,
        editJournalEntryRq: EditJournalEntryRq,
        principal: QyogaUserDetails,
    ): JournalEntry? {
        val therapeuticTask = therapeuticTasksRepo.getOrCreate(
            TherapeuticTask(principal.id, editJournalEntryRq.therapeuticTaskName)
        )

        val query = query {
            JournalEntry::clientRef isEqual clientId
            JournalEntry::id isEqual entryId
        }
        val persistedEntry =
            journalsRepo.updateOne(query) { entry -> entry.updatedBy(editJournalEntryRq, therapeuticTask) }
                ?: error("Entry $entryId for client $clientId not found")

        return persistedEntry
    }

}