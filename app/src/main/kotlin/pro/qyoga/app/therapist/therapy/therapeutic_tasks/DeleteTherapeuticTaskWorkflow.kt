package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.azhidkov.platform.spring.sdj.withSortBy
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo

private val referencesPageRequest = Pageable.ofSize(4).withSortBy(JournalEntry::createdAt)

@Component
class DeleteTherapeuticTaskWorkflow(
    private val therapeuticTasksRepo: TherapeuticTasksRepo,
    private val journalEntriesRepo: JournalEntriesRepo,
) : (Long) -> Unit {

    override fun invoke(taskId: Long) {
        val hasReferencesError = hasReferencesTo(taskId)
        if (hasReferencesError != null) {
            throw hasReferencesError
        }

        therapeuticTasksRepo.deleteById(taskId)
    }

    private fun hasReferencesTo(taskId: Long): TherapeuticTaskHasReferences? {
        val existingReferences =
            journalEntriesRepo.findSlice(referencesPageRequest, fetch = JournalEntry.Fetch.client) {
                JournalEntry::therapeuticTask isEqual AggregateReference.to(taskId)
            }
        val task = { therapeuticTasksRepo.findByIdOrNull(taskId) }

        return if (existingReferences.hasContent()) {
            return TherapeuticTaskHasReferences(
                task()?.ref() ?: error("References $existingReferences to not existing task with id=$taskId"),
                existingReferences.content
            )
        } else {
            null
        }
    }

}