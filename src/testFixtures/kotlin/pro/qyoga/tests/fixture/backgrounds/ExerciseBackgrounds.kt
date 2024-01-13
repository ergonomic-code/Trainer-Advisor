package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.therapy.exercises.ExercisesListPageController
import pro.qyoga.core.therapy.exercises.api.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID

@Component
class ExerciseBackgrounds(
    private val exercisesService: ExercisesService,
    private val exercisesListPageController: ExercisesListPageController
) {

    fun createExercises(exercises: List<CreateExerciseRequest>) {
        exercisesService.addExercises(exercises.map { it to emptyMap() }, THE_THERAPIST_ID)
    }

    fun findExercise(exerciseSearchDto: ExerciseSearchDto): ExerciseSummaryDto? {
        val model = ExtendedModelMap()
        exercisesListPageController.getExercisesFiltered(exerciseSearchDto, Pageable.ofSize(2), model)
        val page = ExercisesListPageController.getExercises(model)
        check(page.content.size <= 1)
        return page.content.firstOrNull()
    }

    fun getExerciseStepImage(exerciseId: Long, stepIdx: Int): ByteArray? {
        return exercisesService.getStepImage(exerciseId, stepIdx)?.use { it.readAllBytes() }
    }

}