package pro.qyoga.core.therapy.exercises.api.dtos

import pro.qyoga.core.therapy.exercises.api.model.ExerciseType
import java.time.Duration

data class ExerciseSummaryDto(
    val title: String,
    val description: String,
    val duration: Duration,
    val type: ExerciseType,
    val id: Long = 0
)