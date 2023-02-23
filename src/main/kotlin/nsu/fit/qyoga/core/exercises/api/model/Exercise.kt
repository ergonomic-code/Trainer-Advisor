package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("exercises")
data class Exercise(
    @Id
    val id: Long,
    val title: String,
    val description: String,
    val indications: String,
    val contradictions: String,
    val duration: String,
    val exerciseType: ExerciseType,
    val therapistId: Long
)
