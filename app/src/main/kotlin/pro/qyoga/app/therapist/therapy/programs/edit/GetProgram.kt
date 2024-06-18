package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.stereotype.Component
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.core.therapy.programs.model.Program


@Component
class GetProgram(
    private val programsRepo: ProgramsRepo,
) : (Long) -> Program? {

    override fun invoke(programId: Long): Program? {
        return programsRepo.findById(programId, fetch = Program.Fetch.therapistOnly)
    }

}