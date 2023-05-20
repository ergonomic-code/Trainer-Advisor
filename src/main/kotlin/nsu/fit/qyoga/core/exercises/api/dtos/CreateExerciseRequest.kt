package nsu.fit.qyoga.core.exercises.api.dtos

data class CreateExerciseRequest(
        val title: String,
        val description: String,
        val indications: String,
        val contradictions: String,
        val duration: Double,
        val exerciseTypeId: Long,
        val steps: List<ExerciseStepDto> = mutableListOf()
)