package nsu.fit.qyoga.core.exercises.api.dtos

data class ExerciseDto(
    val id: Long,
    val title: String,
    val description: String,
    val indications: String,
    val contradictions: String,
    val duration: String,
    val type: String,
    val purpose: String
)
