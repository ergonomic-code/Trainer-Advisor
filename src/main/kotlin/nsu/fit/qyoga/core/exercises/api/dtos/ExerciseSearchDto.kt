package nsu.fit.qyoga.core.exercises.api.dtos

data class ExerciseSearchDto(
    val title: String? = null,
    val contradiction: String? = null,
    val exerciseTypeId: Long? = null,
    val therapeuticPurpose: String? = null,
)
