package pro.qyoga.core.therapy.exercises.api.dtos

import pro.qyoga.core.therapy.exercises.api.model.ExerciseStep

data class CreateExerciseRequest(
    val summary: ExerciseSummaryDto,
    val steps: List<ExerciseStep>
)