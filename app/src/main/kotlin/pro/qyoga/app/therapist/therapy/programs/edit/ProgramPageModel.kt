package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.ModelAndViewBuilder
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrNull
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.model.Program

const val NOT_EXISTING_THERAPEUTIC_TASK = "notExistingTherapeuticTask"

data class ProgramPageModel(
    val id: Long,
    val title: String,
    val therapeuticTaskName: String,
    val exercises: List<ExerciseSummaryDto>
) {

    constructor(program: Program) : this(
        program.id,
        program.title,
        program.therapeuticTaskRef.resolveOrThrow().name,
        program.exercises.mapNotNull { it.exerciseRef.resolveOrNull()?.toSummaryDto() })

    constructor(createProgramRequest: CreateProgramRequest, therapeuticTaskName: String, programId: Long = 0) : this(
        programId,
        createProgramRequest.title,
        therapeuticTaskName,
        emptyList()
    )

}

fun programPageModelAndView(
    pageMode: EntityPageMode,
    program: ProgramPageModel? = null,
    fragment: String? = null,
    additionalModel: ModelAndViewBuilder.() -> Unit = {}
): ModelAndView {
    val modelAndView =
        modelAndView("therapist/therapy/programs/program-edit.html${fragment?.let { " :: $it" } ?: ""}") {
            "program" bindTo program
            "pageMode" bindTo pageMode.name
            additionalModel()
        }
    return modelAndView
}
