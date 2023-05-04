package nsu.fit.qyoga.app.therapist.exercises

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/exercises/create")
class CreateExercisePageController {

    @GetMapping
    fun getCreateExercisePage(): String {
        return "therapist/exercises/exercise-create"
    }

    @PostMapping
    fun createExercise(): String {
        return "redirect:/exercises/exercise-search"
    }

}