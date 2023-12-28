package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService
import pro.qyoga.core.therapy.therapeutic_tasks.internal.TherapeuticTasksRepo


@Component
class TherapeuticTasksBackgrounds(
    private val therapeuticTasksService: TherapeuticTasksService,
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @Transactional
    fun createTherapeuticTask(therapistId: Long, taskName: String): TherapeuticTask {
        return therapeuticTasksService.getOrCreate(therapistId, taskName)
    }

    @Transactional
    fun createTherapeuticTasks(theTherapistId: Long, taskNames: List<String>): List<TherapeuticTask> {
        return taskNames.map { createTherapeuticTask(theTherapistId, it) }
    }

    fun findById(id: Long): TherapeuticTask? {
        return therapeuticTasksRepo.findByIdOrNull(id)
    }

}