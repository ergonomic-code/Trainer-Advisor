package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.platform.notFound
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.getSummaryById
import pro.qyoga.core.therapy.programs.model.ProgramRef


@Controller
class EditProgramPageController(
    private val programsRepo: ProgramsRepo,
    private val editProgram: EditProgram
) {

    @GetMapping(PATH)
    fun getEditProgramPage(@PathVariable programId: Long): ModelAndView {
        val program = programsRepo.getSummaryById(programId)
            ?: return notFound
        return programPageModelAndView(EntityPageMode.EDIT, ProgramPageModel(program))
    }

    @PutMapping(PATH)
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
                    fragment = "programForm",
                    mapOf(
                        NOT_EXISTING_THERAPEUTIC_TASK to true
                    )
                )

            is EditProgramResult.Success ->
                hxRedirect("/therapist/programs")
        }

    companion object {

        const val PATH = "/therapist/programs/{programId}"

        fun editUrlFor(programRef: ProgramRef) = PATH.replace("{programId}", programRef.id.toString())

    }

}
