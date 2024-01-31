package pro.qyoga.app.therapist.appointments.core

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.common.EntityPageMode
import pro.qyoga.app.components.toComboBoxItem
import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.platform.spring.http.hxRedirect


@Controller
@RequestMapping("/therapist/appointments/new")
class CreateAppointmentPageController(
    private val createAppointment: CreateAppointment,
    private val timeZones: TimeZones
) {

    @GetMapping
    fun getCreateTrainingSessionPage(): ModelAndView {
        return appointmentPageModelAndView(
            pageMode = EntityPageMode.CREATE,
            allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem)
        )
    }

    @PostMapping
    fun createAppointment(
        editAppointmentRequest: EditAppointmentRequest,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): Any {
        createAppointment(therapist.ref, editAppointmentRequest)
        return hxRedirect(SchedulePageController.PATH)
    }

}