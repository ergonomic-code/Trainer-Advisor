package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.publc.components.toComboBoxItem
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.core.appointments.core.AppointmentsIntersectionException
import pro.qyoga.core.appointments.core.EditAppointmentRequest
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref


@Controller
@RequestMapping("/therapist/appointments/new")
class CreateAppointmentPageController(
    private val createAppointment: CreateAppointmentWorkflow,
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
        return try {
            createAppointment(therapist.ref, editAppointmentRequest)
            hxRedirect(SchedulePageController.PATH)
        } catch (ex: AppointmentsIntersectionException) {
            appointmentPageModelAndView(
                pageMode = EntityPageMode.CREATE,
                allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem)
            ) {
                "appointment" bindTo editAppointmentRequest
                "appointmentsIntersectionError" bindTo true
                "existingAppointment" bindTo ex.existingAppointment
            }
        }
    }

}