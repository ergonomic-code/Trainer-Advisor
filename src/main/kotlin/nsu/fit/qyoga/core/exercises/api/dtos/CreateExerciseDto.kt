package nsu.fit.qyoga.core.exercises.api.dtos

import nsu.fit.qyoga.core.exercises.api.model.ExerciseType

data class CreateExerciseDto(
    val title: String? = null,
    val description: String? = null,
    val indications: String? = null,
    val contradiction: String? = null,
    val duration: String? = null,
    val exerciseType: ExerciseType? = null,
    val therapeuticPurpose: String? = null,
    val exerciseSteps: List<ExerciseStepDto> = listOf()
)
