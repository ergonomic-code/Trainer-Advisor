package nsu.fit.qyoga.core.exercises.ports

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/exercises/")
class ExercisesController(
    private val exercisesService: ExercisesService
) {

    /**
     * Отображение страницы со списком упражнений
     */
    @GetMapping("all")
    fun getExercisesPage(): String {
        return "ExercisesSearch"
    }

    /**
     * Получение списка упражнений
     */
    @GetMapping
    fun getExercises(
        @RequestParam(value = "title", required = false) title: String?,
        @RequestParam(value = "contraindication", required = false) contradiction: String?,
        @RequestParam(value = "duration", required = false) duration: String?,
        @RequestParam(value = "exerciseType", required = false) exerciseType: ExerciseType?,
        @RequestParam(value = "therapeuticPurpose", required = false) therapeuticPurpose: String?
    ): List<Exercise> {
        return exercisesService.getExercises(title, contradiction, duration, exerciseType, therapeuticPurpose)
    }

    /**
     * Получение типов упражнений
     */
    @GetMapping("types")
    fun getExerciseTypes(): List<ExerciseType> {
        return exercisesService.getExerciseTypes()
    }
}
