package nsu.fit.qyoga.core.exercises.api.dtos

import nsu.fit.qyoga.core.exercises.api.model.ExerciseType

data class ExerciseSearchDto(
    val title: String? = null,
    val contradiction: String? = null,
    val duration: String? = null,
    val exerciseType: ExerciseType? = null,
    val therapeuticPurpose: String? = null,
)