package nsu.fit.qyoga.core.exercises.api.dtos

import nsu.fit.qyoga.core.exercises.api.model.ExerciseType

data class CreateExerciseDto(
    val title: String,
    val description: String,
    val indications: String,
    val contradiction: String,
    val duration: String,
    val exerciseType: ExerciseType,
    val therapeuticPurpose: String,
    val exerciseSteps: List<ExerciseStepDto>
)