package pro.qyoga.core.therapy.programs.model

import org.springframework.data.annotation.*
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.users.api.Therapist
import java.time.Instant

@Immutable
@Table("programs")
data class Program(
    val title: String,
    val therapeuticTaskRef: AggregateReference<TherapeuticTask, Long>,
    val ownerRef: AggregateReference<Therapist, Long>,
    @MappedCollection(idColumn = "program_id", keyColumn = "exercise_index")
    val exercises: List<ProgramExercise>,

    @Id
    val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) {

    companion object {
        fun of(
            createExerciseRequest: CreateProgramRequest,
            therapeuticTask: AggregateReference<TherapeuticTask, Long>,
            therapist: AggregateReference<Therapist, Long>
        ): Program = Program(
            createExerciseRequest.title,
            therapeuticTask,
            therapist,
            createExerciseRequest.exerciseIds.map { ProgramExercise(AggregateReference.to(it)) })
    }

    object Fetch {
        val therapistOnly = listOf(Program::therapeuticTaskRef)
    }

}
