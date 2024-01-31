package pro.qyoga.app.therapist.appointments.core

import org.springframework.web.servlet.ModelAndView
import pro.qyoga.app.common.EntityPageMode
import pro.qyoga.app.components.combobox.ComboBoxItem
import pro.qyoga.platform.spring.mvc.modelAndView


fun appointmentPageModelAndView(
    pageMode: EntityPageMode,
    allAvailableTimeZones: List<ComboBoxItem>
): ModelAndView {
    return modelAndView("therapist/appointments/appointment-edit.html") {
        "pageMode" bindTo pageMode.name
        "allAvailableTimeZones" bindTo allAvailableTimeZones
    }
}
