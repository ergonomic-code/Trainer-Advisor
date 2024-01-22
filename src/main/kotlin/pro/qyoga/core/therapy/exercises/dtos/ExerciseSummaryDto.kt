package pro.qyoga.core.therapy.exercises.dtos

import pro.qyoga.core.therapy.exercises.model.ExerciseType
import java.time.Duration

data class ExerciseSummaryDto(
    val title: String,
    val description: String,
    val duration: Duration,
    val type: ExerciseType,
    val id: Long = 0
)