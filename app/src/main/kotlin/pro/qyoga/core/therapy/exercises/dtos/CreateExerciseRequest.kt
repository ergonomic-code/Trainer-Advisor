package pro.qyoga.core.therapy.exercises.dtos

import pro.qyoga.core.therapy.exercises.model.ExerciseStep

data class CreateExerciseRequest(
    val summary: ExerciseSummaryDto,
    val steps: List<ExerciseStep>
)