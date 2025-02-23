package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.model.ExerciseType


data class ExerciseListPageModel(
    val exercises: Page<ExerciseSummaryDto>,
    val searchDto: ExerciseSearchDto = ExerciseSearchDto(),
    val fragment: String? = null
) : ModelAndView(
    viewId("therapist/therapy/exercises/exercises-list", fragment), mapOf(
        "searchDto" to ExerciseSearchDto(),
        "types" to ExerciseType.entries,
        "exercises" to exercises,
        "pageNumbers" to (1..exercises.totalPages).toList()
    )
)

@Controller
class ExercisesListPageController(
    private val exercisesService: ExercisesService
) {

    @GetMapping("/therapist/exercises")
    fun getExercises(
        @PageableDefault(value = 10, page = 0) page: Pageable,
    ): ExerciseListPageModel {
        val exercises = exercisesService.findExerciseSummaries(ExerciseSearchDto.ALL, page)
        return ExerciseListPageModel(exercises)
    }

    @GetMapping("/therapist/exercises/search")
    fun getExercisesFiltered(
        searchDto: ExerciseSearchDto,
        @PageableDefault(value = 10, page = 0) page: Pageable
    ): ExerciseListPageModel {
        val exercises = exercisesService.findExerciseSummaries(searchDto, page)
        return ExerciseListPageModel(exercises, searchDto, "exercises")
    }

    @DeleteMapping("/therapist/exercises/{exerciseId}")
    @ResponseBody
    fun deleteExercise(
        @PathVariable exerciseId: Long
    ): Any {
        exercisesService.deleteById(exerciseId)
        return ResponseEntity.ok(null)
    }


}
