package pro.qyoga.core.therapy.programs.model

import org.springframework.data.annotation.*
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.api.Therapist
import pro.qyoga.platform.spring.sdj.erpo.hydration.FetchSpec
import pro.qyoga.platform.spring.sdj.erpo.hydration.hydrate
import java.time.Instant

@Immutable
@Table("programs")
data class Program(
    val title: String,
    val therapeuticTaskRef: TherapeuticTaskRef,
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

    fun patchBy(updateProgramRequest: CreateProgramRequest, therapeuticTaskRef: TherapeuticTaskRef): Program {
        val updatedExercises = updateProgramRequest.exerciseIds.map {
            ProgramExercise(AggregateReference.to(it))
        }

        return Program(
            updateProgramRequest.title,
            therapeuticTaskRef,
            ownerRef,
            updatedExercises,
            id,
            createdAt,
            modifiedAt,
            version
        )
    }

    companion object {
        fun of(
            createExerciseRequest: CreateProgramRequest,
            therapeuticTask: TherapeuticTaskRef,
            therapist: AggregateReference<Therapist, Long>,
            programId: Long = 0
        ): Program = Program(
            createExerciseRequest.title,
            therapeuticTask,
            therapist,
            createExerciseRequest.exerciseIds.map { ProgramExercise(AggregateReference.to(it)) },
            programId
        )
    }

    object Fetch {
        val therapistOnly = listOf(Program::therapeuticTaskRef)
    }

}

fun Program.fetchExercises(jdbcAggregateOperations: JdbcAggregateOperations): Program {
    return this.copy(
        exercises = jdbcAggregateOperations.hydrate(
            this.exercises,
            FetchSpec(listOf(ProgramExercise::exerciseRef))
        )
    )
}

