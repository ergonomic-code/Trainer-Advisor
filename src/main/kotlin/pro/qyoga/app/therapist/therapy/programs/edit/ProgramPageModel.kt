package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.platform.spring.mvc.ModelAndViewBuilder
import pro.qyoga.platform.spring.mvc.modelAndView


enum class ProgramPageMode {
    CREATE,
    EDIT
}

fun programPageModel(
    pageMode: ProgramPageMode,
    program: Program? = null,
    fragment: String? = null,
    additionalModel: ModelAndViewBuilder.() -> Unit = {}
): ModelAndView {
    val modelAndView =
        modelAndView("/therapist/therapy/programs/program-edit.html${fragment?.let { " :: $it" } ?: ""}") {
            "program" bindTo program
            "pageMode" bindTo pageMode.name
            additionalModel()
        }
    return modelAndView
}