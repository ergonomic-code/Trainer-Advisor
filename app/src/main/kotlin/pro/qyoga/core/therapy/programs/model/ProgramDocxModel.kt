package pro.qyoga.core.therapy.programs.model

import pro.qyoga.core.therapy.exercises.model.ExerciseStep

data class DocxProgram(
    val id: Long,
    val title: String,
    val exercises: List<DocxExercise>
)

data class DocxExercise(
    val id: Long,
    val title: String,
    val description: String,
    val steps: List<ExerciseStep>
)