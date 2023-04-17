package nsu.fit.qyoga.core.programs.api.dtos

import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto

data class ProgramWithExercisesDto(
    val id: Long,
    val title: String,
    val date: String,
    val purpose: String,
    val exercises: List<ExerciseDto>
)