package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import pro.qyoga.core.therapy.exercises.api.ExerciseDto
import pro.qyoga.core.therapy.exercises.api.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.ExerciseType
import pro.qyoga.core.therapy.exercises.api.ExercisesService

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
        val exercises = exercisesService.findExercises(ExerciseSearchDto.ALL, page)
        model.addAllAttributes(toModelAttributes(exercises))
        return "therapist/therapy/exercises/exercises-list"
    }

    @GetMapping("/search")
    fun getExercisesFiltered(
        searchDto: ExerciseSearchDto,
        @PageableDefault(value = 10, page = 0) page: Pageable,
        model: Model
    ): String {
        val exercises = exercisesService.findExercises(searchDto, page)
        model.addAllAttributes(toModelAttributes(exercises))
        return "therapist/therapy/exercises/exercises-list :: exercises"
    }

    fun toModelAttributes(exercises: Page<ExerciseDto>): Map<String, Any> =
        mapOf(
            "searchDto" to ExerciseSearchDto(),
            "types" to ExerciseType.entries,
            EXERCISES to exercises,
            "pageNumbers" to (1..exercises.totalPages).toList()
        )

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun getExercises(model: Model): Page<ExerciseDto> = model.getAttribute(EXERCISES) as Page<ExerciseDto>

    }

}
