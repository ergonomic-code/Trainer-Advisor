package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService


@Component
class TherapeuticTasksBackgrounds(
    private val therapeuticTasksService: TherapeuticTasksService
) {

    @Transactional
    fun createTherapeuticTask(therapistId: Long, taskName: String): TherapeuticTask {
        return therapeuticTasksService.getOrCreate(therapistId, taskName)
    }

    @Transactional
    fun createTherapeuticTasks(theTherapistId: Long, taskNames: List<String>): List<TherapeuticTask> {
        return taskNames.map { createTherapeuticTask(theTherapistId, it) }
    }

}