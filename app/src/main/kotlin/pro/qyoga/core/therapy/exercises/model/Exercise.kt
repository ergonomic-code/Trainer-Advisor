package pro.qyoga.core.therapy.exercises.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.ergo.hydration.Identifiable
import pro.qyoga.core.therapy.exercises.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Duration
import java.time.Instant
import java.util.*

@Table("exercises")
data class Exercise(
    val title: String,
    val description: String,
    val duration: Duration,
    val exerciseType: ExerciseType,
    val ownerRef: TherapistRef,
    @MappedCollection(idColumn = "exercise_id", keyColumn = "step_index")
    val steps: List<ExerciseStep>,

    @Id
    override val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) : Identifiable<Long> {

    fun toSummaryDto() =
        ExerciseSummaryDto(title, description, duration, exerciseType, id)

    fun patchBy(exerciseSummaryDto: ExerciseSummaryDto): Exercise =
        copy(
            title = exerciseSummaryDto.title,
            description = exerciseSummaryDto.description,
            duration = exerciseSummaryDto.duration,
            exerciseType = exerciseSummaryDto.type,
        )

    companion object {

        fun of(
            createExerciseRequest: CreateExerciseRequest,
            persistedImages: Map<Int, Long>,
            therapistId: UUID
        ): Exercise {
            val stepsWithRefs = createExerciseRequest.steps.mapIndexed { idx, dto ->
                dto.withImage(persistedImages[idx]?.let { AggregateReference.to(it) })
            }

            return Exercise(
                createExerciseRequest.summary.title,
                createExerciseRequest.summary.description,
                createExerciseRequest.summary.duration,
                createExerciseRequest.summary.type,
                TherapistRef.to(therapistId),
                stepsWithRefs
            )
        }
    }

}
