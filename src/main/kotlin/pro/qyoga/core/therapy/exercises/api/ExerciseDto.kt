package pro.qyoga.core.therapy.exercises.api

import java.time.Duration

data class ExerciseDto(
    val id: Long,
    val title: String,
    val description: String,
    val duration: Duration,
    val type: ExerciseType,
)