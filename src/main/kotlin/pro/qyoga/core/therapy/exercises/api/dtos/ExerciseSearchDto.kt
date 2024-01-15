package pro.qyoga.core.therapy.exercises.api.dtos

import pro.qyoga.core.therapy.exercises.api.model.ExerciseType

data class ExerciseSearchDto(
    val title: String? = null,
    val exerciseType: ExerciseType? = null,
) {

    companion object {

        val ALL = ExerciseSearchDto()

    }

}
