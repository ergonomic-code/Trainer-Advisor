package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.therapy.programs.impl.ProgramsRepo
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.findByName


sealed interface CreateProgramResult {

    data class InvalidTherapeuticTaskName(val therapeuticTaskName: String) : CreateProgramResult
    data class Success(val program: Program) : CreateProgramResult

}

@Component
class CreateProgram(
    private val programsRepo: ProgramsRepo,
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) : (CreateProgramRequest, String, Long) -> CreateProgramResult {

    override fun invoke(
        createExerciseRequest: CreateProgramRequest,
        therapeuticTaskName: String,
        therapistId: Long
    ): CreateProgramResult {
        val therapeuticTask = therapeuticTasksRepo.findByName(therapeuticTaskName)
            ?: return CreateProgramResult.InvalidTherapeuticTaskName(therapeuticTaskName)

        val program = Program.of(
            createExerciseRequest,
            therapeuticTask.ref(),
            AggregateReference.to(therapistId),
        )

        return CreateProgramResult.Success(programsRepo.save(program))
    }

}

