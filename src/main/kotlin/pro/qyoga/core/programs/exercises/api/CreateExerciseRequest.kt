package pro.qyoga.core.programs.exercises.api

data class CreateExerciseRequest(
    val title: String,
    val description: String,
    val duration: Double,
    val exerciseType: ExerciseType,
    val steps: List<ExerciseStepDto> = mutableListOf()
)