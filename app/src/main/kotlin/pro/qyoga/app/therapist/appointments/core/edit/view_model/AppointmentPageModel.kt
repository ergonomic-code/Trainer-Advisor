package pro.qyoga.app.therapist.appointments.core.edit.view_model

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.platform.components.combobox.ComboBoxItem


fun appointmentPageModelAndView(
    pageMode: EntityPageMode,
    allAvailableTimeZones: List<ComboBoxItem>,
    additionalModel: Map<String, Any> = emptyMap()
): ModelAndView {
    return modelAndView(
        "therapist/appointments/appointment-edit.html",
        mapOf(
            "pageMode" to pageMode.name,
            "allAvailableTimeZones" to allAvailableTimeZones
        ) + additionalModel
    )
}
