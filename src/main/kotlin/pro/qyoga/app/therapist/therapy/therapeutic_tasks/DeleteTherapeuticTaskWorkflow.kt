package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.qyoga.core.clients.cards.internal.ClientsRepo
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.hydrate
import pro.qyoga.core.clients.journals.internal.JournalEntriesRepo
import pro.qyoga.core.therapy.therapeutic_tasks.internal.TherapeuticTasksRepo
import pro.qyoga.platform.spring.sdj.ref
import pro.qyoga.platform.spring.sdj.withSortBy


@Component
class DeleteTherapeuticTaskWorkflow(
    private val therapeuticTasksRepo: TherapeuticTasksRepo,
    private val journalEntriesRepo: JournalEntriesRepo,
    private val clientsRepo: ClientsRepo
) : (Long) -> Unit {

    override fun invoke(taskId: Long) {
        val references =
            journalEntriesRepo.findByTherapeuticTask(taskId, Pageable.ofSize(4).withSortBy(JournalEntry::createdAt))
        if (!references.isEmpty) {
            val referencingEntries = references.content.hydrate(fetchClients = clientsRepo::findMapById)
            val task = therapeuticTasksRepo.findByIdOrNull(taskId)
                ?: error("References to not existing task")
            throw TherapeuticTaskHasReferences(task.ref(), referencingEntries)
        }

        therapeuticTasksRepo.deleteById(taskId)
    }

}