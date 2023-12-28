package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.stereotype.Component
import pro.qyoga.core.therapy.therapeutic_tasks.internal.TherapeuticTasksRepo


@Component
class DeleteTherapeuticTaskWorkflow(
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) : (Long) -> Unit {

    override fun invoke(taskId: Long) {
        therapeuticTasksRepo.deleteById(taskId)
    }

}