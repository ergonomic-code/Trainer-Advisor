package nsu.fit.qyoga.core.exercises.api.dtos

import nsu.fit.qyoga.core.exercises.api.model.ExerciseType

data class ExerciseDto(
    val id: Long,
    val title: String,
    val description: String,
    val indications: String,
    val contradictions: String,
    val duration: String,
    val type: ExerciseType,
    val purpose: String
)
