package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref


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
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ExerciseListPageModel {
        val exercises = exercisesService.findExerciseSummaries(principal.ref, ExerciseSearchDto.ALL, page)
        return ExerciseListPageModel(exercises)
    }

    @GetMapping("/therapist/exercises/search")
    fun getExercisesFiltered(
        searchDto: ExerciseSearchDto,
        @PageableDefault(value = 10, page = 0) page: Pageable,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ExerciseListPageModel {
        val exercises = exercisesService.findExerciseSummaries(principal.ref, searchDto, page)
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
