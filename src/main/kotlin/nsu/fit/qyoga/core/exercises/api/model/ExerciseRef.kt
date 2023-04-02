package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("exercise_purposes")
data class ExerciseRef(
    @Column("exercise_id")
    val exerciseId: Long
)
