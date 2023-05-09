package nsu.fit.qyoga.core.exercises.api.dtos

import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import java.time.Duration

data class ExerciseDto(
    val id: Long,
    val title: String,
    val description: String,
    val indications: String,
    val contradictions: String,
    val duration: Duration,
    val type: ExerciseType,
    val purpose: String
)
