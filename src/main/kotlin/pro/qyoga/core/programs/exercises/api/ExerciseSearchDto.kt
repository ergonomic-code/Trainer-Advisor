package pro.qyoga.core.programs.exercises.api

data class ExerciseSearchDto(
    val title: String? = null,
    val exerciseType: ExerciseType? = null,
) {

    companion object {

        val ALL = ExerciseSearchDto()

    }

}
