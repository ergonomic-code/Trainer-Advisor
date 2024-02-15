package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.platform.notFound
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest


@Controller
@RequestMapping("/therapist/programs/{programId}")
class EditProgramPageController(
    private val getProgram: GetProgram,
    private val editProgram: EditProgram
) {

    @GetMapping
    fun getEditProgramPage(@PathVariable programId: Long): ModelAndView {
        val program = getProgram(programId)
            ?: return notFound
        return programPageModelAndView(EntityPageMode.EDIT, ProgramPageModel(program))
    }

    @PutMapping
    fun handleEditProgram(
        @PathVariable programId: Long,
        updateProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String
    ): Any =
        when (this.editProgram(programId, updateProgramRequest, therapeuticTaskName)) {
            is EditProgramResult.InvalidTherapeuticTaskName ->
                programPageModelAndView(
                    pageMode = EntityPageMode.EDIT,
                    program = ProgramPageModel(updateProgramRequest, therapeuticTaskName, programId),
                    fragment = "programForm"
                ) {
                    NOT_EXISTING_THERAPEUTIC_TASK bindTo true
                }

            is EditProgramResult.ProgramNotFound ->
                notFound

            is EditProgramResult.Success ->
                hxRedirect("/therapist/programs")
        }

}