package pro.qyoga.core.therapy.exercises.internal

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.therapy.exercises.api.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.api.ExerciseType
import pro.qyoga.platform.java.time.toDurationMinutes
import java.time.Duration
import java.time.Instant

@Table("exercises")
data class Exercise(
    val title: String,
    val description: String,
    val duration: Duration,
    val exerciseType: ExerciseType,
    val therapistId: Long,
    @MappedCollection(idColumn = "exercise_id", keyColumn = "step_index")
    val steps: List<ExerciseStep>,

    @Id
    val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) {

    fun toDto() =
        ExerciseSummaryDto(id, title, description, duration, exerciseType)

    companion object {

        fun of(
            createExerciseRequest: CreateExerciseRequest,
            persistedImages: Map<Int, Long>,
            therapistId: Long
        ): Exercise {
            val exerciseSteps = createExerciseRequest.steps.mapIndexed { idx, dto ->
                ExerciseStep(dto.description, persistedImages[idx])
            }

            return Exercise(
                createExerciseRequest.title,
                createExerciseRequest.description,
                createExerciseRequest.duration.toDurationMinutes(),
                createExerciseRequest.exerciseType,
                therapistId,
                exerciseSteps
            )
        }
    }

}
