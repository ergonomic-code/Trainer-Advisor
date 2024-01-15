package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.app.common.notFound
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.api.errors.ExerciseNotFound
import pro.qyoga.core.therapy.exercises.api.errors.ExerciseStepNotFound
import pro.qyoga.platform.spring.http.hxRedirect
import pro.qyoga.platform.spring.mvc.modelAndView

@Controller
@RequestMapping("/therapist/exercises/{exerciseId}")
class EditExercisePageController(
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getEditExercisePage(@PathVariable exerciseId: Long): ModelAndView {
        val res = runCatching {
            exercisesService.findById(exerciseId)
        }

        val exercise = when (val ex = res.exceptionOrNull()) {
            is ExerciseNotFound ->
                return notFound

            else ->
                res.getOrThrow()
        }

        return modelAndView("therapist/therapy/exercises/exercise-create") {
            "exercise" bindTo exercise
        }
    }

    @PutMapping
    fun editExercise(
        @PathVariable exerciseId: Long,
        @ModelAttribute exerciseSummaryDto: ExerciseSummaryDto,
    ): ResponseEntity<Unit> {

        exercisesService.updateExercise(exerciseId, exerciseSummaryDto)

        return hxRedirect("/therapist/exercises")
    }

    @GetMapping("/step-images/{stepIdx}")
    fun getStepImage(
        @PathVariable exerciseId: Long,
        @PathVariable stepIdx: Int
    ): Any {
        val res = runCatching {
            exercisesService.getStepImage(exerciseId, stepIdx)
        }

        val imageStream = when (res.exceptionOrNull()) {
            is ExerciseNotFound, is ExerciseStepNotFound ->
                return notFound

            else ->
                res.getOrThrow()!!
        }

        return ResponseEntity.ok()
            .header("Content-Type", imageStream.metaData.mediaType)
            .header("Content-Length", imageStream.metaData.size.toString())
            .body(InputStreamResource(imageStream.inputStream))
    }

}