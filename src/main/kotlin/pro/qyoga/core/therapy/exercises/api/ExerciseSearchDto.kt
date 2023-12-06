package pro.qyoga.core.therapy.exercises.api

data class ExerciseSearchDto(
    val title: String? = null,
    val exerciseType: ExerciseType? = null,
) {

    companion object {

        val ALL = ExerciseSearchDto()

    }

}
