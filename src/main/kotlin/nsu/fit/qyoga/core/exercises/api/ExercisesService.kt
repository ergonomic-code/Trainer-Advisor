package nsu.fit.qyoga.core.exercises.api

import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType

interface ExercisesService {
    fun getExercises(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticPurpose: String?
    ): List<Exercise>

    fun getExerciseTypes(): List<ExerciseType>
}
