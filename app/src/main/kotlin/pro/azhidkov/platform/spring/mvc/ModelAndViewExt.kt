package pro.azhidkov.platform.spring.mvc

import org.springframework.ui.ModelMap
import org.springframework.web.servlet.ModelAndView

class ModelBuilder(
    private val modelMap: ModelMap
) {

    infix fun String.bindTo(value: Any?) {
        modelMap.addAttribute(this, value)
    }

}

fun modelAndView(viewName: String, buildModel: ModelBuilder.() -> Unit = {}): ModelAndView {
    val modelAndView = ModelAndView(viewName)
    val builder = ModelBuilder(modelAndView.modelMap)
    builder.buildModel()
    return modelAndView
}

fun modelAndView(viewName: String, model: ModelMap): ModelAndView {
    val modelAndView = ModelAndView(viewName)
    modelAndView.modelMap.mergeAttributes(model)
    return modelAndView
}

fun model(buildModel: ModelBuilder.() -> Unit = {}): ModelMap {
    val model = ModelMap()
    val builder = ModelBuilder(model)
    builder.buildModel()
    return model
}