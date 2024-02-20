package pro.qyoga.app.therapist.therapy.exercises.mapping

import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder
import pro.azhidkov.platform.java.time.toDurationMinutes
import java.beans.PropertyEditorSupport
import java.time.Duration


@ControllerAdvice("pro.qyoga.app.therapist.therapy.exercises")
class DurationBindingAdvice {

    @InitBinder
    fun initBinders(binder: WebDataBinder) {
        binder.registerCustomEditor(Duration::class.java, object : PropertyEditorSupport() {
            override fun setAsText(text: String) {
                this.value = text.toDouble().toDurationMinutes()
            }
        })
    }

}