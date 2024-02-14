package pro.qyoga.app.therapist.appointments.core.edit.mapping

import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder
import java.beans.PropertyEditorSupport
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@ControllerAdvice("pro.qyoga.app.therapist.appointments")
class AppointmentDurationBindingAdvice {

    @InitBinder
    fun initBinders(binder: WebDataBinder) {
        binder.registerCustomEditor(Duration::class.java, object : PropertyEditorSupport() {
            override fun setAsText(text: String) {
                this.value =
                    Duration.ofSeconds(LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME).toSecondOfDay().toLong())
            }
        })
    }

}
