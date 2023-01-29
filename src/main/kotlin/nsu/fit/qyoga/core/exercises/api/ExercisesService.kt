package nsu.fit.qyoga.core.exercises.api

import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.Page

interface ExercisesService {
    fun getExercises(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticTask: String?
    ): Page<Exercise>
}