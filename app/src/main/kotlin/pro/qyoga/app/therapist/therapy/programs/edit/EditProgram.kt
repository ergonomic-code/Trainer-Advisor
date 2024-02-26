package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.therapy.programs.impl.ProgramsRepo
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.findByName

sealed interface EditProgramResult {
    data class InvalidTherapeuticTaskName(val therapeuticTaskName: String) : EditProgramResult
    data class ProgramNotFound(val programId: Long) : EditProgramResult
    data class Success(val program: Program) : EditProgramResult
}

@Component
class EditProgram(
    private val programsRepo: ProgramsRepo,
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) : (Long, CreateProgramRequest, String) -> EditProgramResult {

    override fun invoke(
        programId: Long,
        updateProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String
    ): EditProgramResult {
        val therapeuticTask = therapeuticTasksRepo.findByName(therapeuticTaskName)
            ?: return EditProgramResult.InvalidTherapeuticTaskName(therapeuticTaskName)

        val updatedProgram = programsRepo.update(programId) {
            it.patchBy(updateProgramRequest, therapeuticTask.ref())
        }

        if (updatedProgram == null) {
            return EditProgramResult.ProgramNotFound(programId)
        }

        return EditProgramResult.Success(updatedProgram)
    }

}