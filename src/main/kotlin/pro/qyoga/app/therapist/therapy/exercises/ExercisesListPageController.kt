package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pro.qyoga.app.common.notFound
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.errors.ExerciseNotFound
import pro.qyoga.core.therapy.exercises.model.ExerciseType

private const val EXERCISES = "exercises"

@Controller
@RequestMapping("/therapist/exercises")
class ExercisesListPageController(
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getExercises(
        @PageableDefault(value = 10, page = 0) page: Pageable,
        model: Model
    ): String {
        val exercises = exercisesService.findExerciseSummaries(ExerciseSearchDto.ALL, page)
        model.addAllAttributes(toModelAttributes(exercises))
        return "therapist/therapy/exercises/exercises-list"
    }

    @GetMapping("/search")
    fun getExercisesFiltered(
        searchDto: ExerciseSearchDto,
        @PageableDefault(value = 10, page = 0) page: Pageable,
        model: Model
    ): String {
        val exercises = exercisesService.findExerciseSummaries(searchDto, page)
        model.addAllAttributes(toModelAttributes(exercises))
        return "therapist/therapy/exercises/exercises-list :: exercises"
    }

    @DeleteMapping("{exerciseId}")
    @ResponseBody
    fun deleteExercise(
        @PathVariable exerciseId: Long
    ): Any {
        val result = runCatching {
            exercisesService.deleteById(exerciseId)
        }

        when (result.exceptionOrNull()) {
            is ExerciseNotFound ->
                return notFound

            else ->
                result.getOrThrow()
        }

        return ResponseEntity.ok(null)
    }

    fun toModelAttributes(exercises: Page<ExerciseSummaryDto>): Map<String, Any> =
        mapOf(
            "searchDto" to ExerciseSearchDto(),
            "types" to ExerciseType.entries,
            EXERCISES to exercises,
            "pageNumbers" to (1..exercises.totalPages).toList()
        )

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun getExercises(model: Model): Page<ExerciseSummaryDto> =
            model.getAttribute(EXERCISES) as Page<ExerciseSummaryDto>

    }

}
