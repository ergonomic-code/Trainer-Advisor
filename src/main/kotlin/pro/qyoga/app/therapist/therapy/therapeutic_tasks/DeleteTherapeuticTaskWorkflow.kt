package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.azhidkov.platform.spring.sdj.query.QueryBuilder
import pro.azhidkov.platform.spring.sdj.withSortBy
import pro.qyoga.core.clients.journals.internal.JournalEntriesRepo
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo

private val referencesPageRequest = Pageable.ofSize(4).withSortBy(JournalEntry::createdAt)

@Component
class DeleteTherapeuticTaskWorkflow(
    private val therapeuticTasksRepo: TherapeuticTasksRepo,
    private val journalEntriesRepo: JournalEntriesRepo,
) : (Long) -> Unit {

    override fun invoke(taskId: Long) {
        ensureNoReferencesExists(taskId)
        therapeuticTasksRepo.deleteById(taskId)
    }

    private fun ensureNoReferencesExists(taskId: Long) {
        val referencesQuery: QueryBuilder.() -> Unit = {
            JournalEntry::therapeuticTask isEqual AggregateReference.to(taskId)
        }
        val referencesExists = journalEntriesRepo.exists(referencesQuery)

        if (referencesExists) {
            val references =
                journalEntriesRepo.findAll(referencesPageRequest, fetch = listOf(JournalEntry::client), referencesQuery)
                    .content
            val task = therapeuticTasksRepo.findByIdOrNull(taskId)
                ?: error("References $references to not existing task with id=$taskId")
            throw TherapeuticTaskHasReferences(task.ref(), references)
        }
    }

}