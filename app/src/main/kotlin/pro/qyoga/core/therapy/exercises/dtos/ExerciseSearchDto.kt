package pro.qyoga.core.therapy.exercises.dtos

import pro.qyoga.core.therapy.exercises.model.ExerciseType

data class ExerciseSearchDto(
    val title: String? = null,
    val exerciseType: ExerciseType? = null,
) {

    companion object {

        val ALL = ExerciseSearchDto()

    }

}
