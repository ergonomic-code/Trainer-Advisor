package pro.qyoga.app.therapist.programs.exercises

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import pro.qyoga.core.programs.exercises.api.ExerciseDto
import pro.qyoga.core.programs.exercises.api.ExerciseSearchDto
import pro.qyoga.core.programs.exercises.api.ExerciseType
import pro.qyoga.core.programs.exercises.api.ExercisesService

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
        return "therapist/exercises/exercise-search"
    }

    @GetMapping("/search")
    fun getExercisesFiltered(
        searchDto: ExerciseSearchDto,
        @PageableDefault(value = 10, page = 0) page: Pageable,
        model: Model
    ): String {
        val exercises = exercisesService.findExercises(searchDto, page)
        model.addAllAttributes(toModelAttributes(exercises))
        return "therapist/exercises/exercise-search :: exercises"
    }

    fun toModelAttributes(exercises: Page<ExerciseDto>): Map<String, Any> =
        mapOf(
            "searchDto" to ExerciseSearchDto(),
            "types" to ExerciseType.entries,
            "exercises" to exercises,
            "pageNumbers" to (1..exercises.totalPages).toList()
        )

}
