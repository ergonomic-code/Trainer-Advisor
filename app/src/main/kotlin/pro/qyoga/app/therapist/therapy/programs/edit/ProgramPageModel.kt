package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.views.ProgramSummaryView

const val NOT_EXISTING_THERAPEUTIC_TASK = "notExistingTherapeuticTask"

data class ProgramPageModel(
    val id: Long, val title: String, val therapeuticTaskName: String, val exercises: List<ExerciseSummaryDto>
) {

    constructor(program: ProgramSummaryView) : this(
        program.id, program.title, program.therapeuticTask.name, program.exercises
    )

    constructor(createProgramRequest: CreateProgramRequest, therapeuticTaskName: String, programId: Long = 0) : this(
        programId, createProgramRequest.title, therapeuticTaskName, emptyList()
    )

}

fun programPageModelAndView(
    pageMode: EntityPageMode,
    program: ProgramPageModel? = null,
    fragment: String? = null,
    additionalModel: Map<String, Any> = emptyMap()
): ModelAndView {
    val modelAndView =
        modelAndView(
            viewId("therapist/therapy/programs/program-edit.html", fragment), mapOf(
                "program" to program,
                "pageMode" to pageMode.name
            ) + additionalModel
        )
    return modelAndView
}
