package pro.azhidkov.platform.spring.mvc

import org.springframework.web.servlet.ModelAndView

class ModelAndViewBuilder(
    val modelAndView: ModelAndView
) {

    infix fun String.bindTo(value: Any?) {
        modelAndView.addObject(this, value)
    }

}

fun modelAndView(viewName: String, buildModel: ModelAndViewBuilder.() -> Unit): ModelAndView {
    val builder = ModelAndViewBuilder(ModelAndView(viewName))
    builder.buildModel()
    return builder.modelAndView
}