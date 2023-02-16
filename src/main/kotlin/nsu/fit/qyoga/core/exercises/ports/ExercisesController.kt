package nsu.fit.qyoga.core.exercises.ports

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.Page
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/exercises/")
class ExercisesController(
    private val exercisesService: ExercisesService
) {
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
    ): Page<Exercise> {
        return exercisesService.getExercises(title, contradiction, duration, exerciseType, therapeuticPurpose)
    }

    /**
     * Создание упражнения
     */
    @PostMapping
    fun createExercise() {

    }

    /**
     * Удаление упражнения
     */
    @DeleteMapping
    fun deleteExercise() {

    }

    /**
     * Редактирование упражнения
     */
    @PutMapping
    fun editExercise() {

    }
}