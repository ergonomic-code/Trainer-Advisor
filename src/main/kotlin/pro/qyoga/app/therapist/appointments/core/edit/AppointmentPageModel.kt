package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.ModelAndViewBuilder
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.platform.components.combobox.ComboBoxItem


fun appointmentPageModelAndView(
    pageMode: EntityPageMode,
    allAvailableTimeZones: List<ComboBoxItem>,
    additionalModel: ModelAndViewBuilder.() -> Unit = {}
): ModelAndView {
    return modelAndView("therapist/appointments/appointment-edit.html") {
        "pageMode" bindTo pageMode.name
        "allAvailableTimeZones" bindTo allAvailableTimeZones
        additionalModel()
    }
}
