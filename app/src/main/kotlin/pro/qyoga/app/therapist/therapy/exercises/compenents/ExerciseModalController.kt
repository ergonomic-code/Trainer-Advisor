package pro.qyoga.app.therapist.therapy.exercises.compenents

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.therapy.exercises.compenents.ExerciseModalController.Companion.PATH
import pro.qyoga.core.therapy.exercises.ExercisesService


@Controller
@RequestMapping(PATH)
class ExerciseModalController(
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getEditExerciseModal(@PathVariable exerciseId: Long): ModelAndView {
        val exercise = exercisesService.findById(exerciseId)
            ?: return notFound

        return modelAndView("therapist/therapy/exercises/exercise-modal") {
            "exercise" bindTo exercise
        }
    }

    companion object {
        const val PATH = "/therapist/exercises/{exerciseId}/modal"
    }

}