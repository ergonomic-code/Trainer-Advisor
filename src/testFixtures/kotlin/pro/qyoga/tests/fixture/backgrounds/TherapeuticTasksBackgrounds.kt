package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.internal.TherapeuticTasksRepo
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID


@Component
class TherapeuticTasksBackgrounds(
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @Transactional
    fun createTherapeuticTask(
        therapistId: Long = THE_THERAPIST_ID,
        taskName: String = randomCyrillicWord()
    ): TherapeuticTask {
        return therapeuticTasksRepo.getOrCreate(TherapeuticTask(therapistId, taskName))
    }

    @Transactional
    fun createTherapeuticTasks(theTherapistId: Long, taskNames: List<String>): List<TherapeuticTask> {
        return taskNames.map { createTherapeuticTask(theTherapistId, it) }
    }

    fun findById(id: Long): TherapeuticTask? {
        return therapeuticTasksRepo.findByIdOrNull(id)
    }

}