package pro.azhidkov.platform.spring.mvc

import org.springframework.web.servlet.ModelAndView

fun modelAndView(viewName: String, model: Map<String, Any?>): ModelAndView {
    val modelAndView = ModelAndView(viewName)
    modelAndView.modelMap.mergeAttributes(model)
    return modelAndView
}

fun viewId(viewName: String, fragment: String? = null) = viewName + (fragment?.let { " :: $it" } ?: "")