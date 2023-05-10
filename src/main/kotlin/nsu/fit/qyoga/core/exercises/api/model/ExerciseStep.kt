package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("exercise_steps")
data class ExerciseStep(
    val description: String,
    val imageId: Long?,
    val stepIndex: Int,
    @Id
    val id: Long = 0
)