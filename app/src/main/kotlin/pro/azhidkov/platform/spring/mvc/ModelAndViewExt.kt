package pro.azhidkov.platform.spring.mvc

import org.springframework.ui.ModelMap
import org.springframework.web.servlet.ModelAndView

@Deprecated("Передавайте Map<String, Any?> явно")
class ModelBuilder(
    private val modelMap: ModelMap
) {

    infix fun String.bindTo(value: Any?) {
        modelMap.addAttribute(this, value)
    }

}

@Deprecated("Передавайте Map<String, Any?> явно")
fun modelAndView(viewName: String, buildModel: ModelBuilder.() -> Unit = {}): ModelAndView {
    val modelAndView = ModelAndView(viewName)
    val builder = ModelBuilder(modelAndView.modelMap)
    builder.buildModel()
    return modelAndView
}

fun modelAndView(viewName: String, model: Map<String, Any?>): ModelAndView {
    val modelAndView = ModelAndView(viewName)
    modelAndView.modelMap.mergeAttributes(model)
    return modelAndView
}
