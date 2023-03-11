package nsu.fit.qyoga.core.exercises.api.dtos

import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import java.time.Duration

data class ExerciseDto(
    val id: Long,
    val title: String,
    val description: String,
    val indications: String,
    val contradictions: String,
    val duration: String,
    val type: ExerciseType,
    val purpose: String
) {
    fun mapToExerciseEntity(): Exercise =
        Exercise(id, title, description, indications, contradictions, Duration.parse(duration), type.id, 1)
}
