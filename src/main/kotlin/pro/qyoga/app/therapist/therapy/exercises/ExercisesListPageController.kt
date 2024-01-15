package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.api.model.ExerciseType

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
