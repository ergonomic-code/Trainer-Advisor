package pro.qyoga.app.infra

import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder


/**
 * Включает во всём приложении конвертацию входящих пустых строк в запросах в null-ы
 */
@ControllerAdvice("pro.qyoga.app")
class EmptyStringToNullControllerAdvice {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(String::class.java, StringTrimmerEditor(true))
    }

}