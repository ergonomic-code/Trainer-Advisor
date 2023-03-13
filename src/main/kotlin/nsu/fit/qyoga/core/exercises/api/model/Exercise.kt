package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Duration

@Table("exercises")
data class Exercise(
    @Id
    val id: Long = 0,
    val title: String,
    val description: String,
    val indications: String,
    val contradictions: String,
    val duration: Duration,
    val exerciseTypeId: Int,
    val therapistId: Long
)
