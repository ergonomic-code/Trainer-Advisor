package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.platform.seeOther
import pro.qyoga.app.publc.components.toComboBoxItem
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController.Companion.calendarForDayWithFocus
import pro.qyoga.core.appointments.core.AppointmentsIntersectionException
import pro.qyoga.core.appointments.core.EditAppointmentRequest
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import java.time.LocalDateTime


@Controller
@RequestMapping(CreateAppointmentPageController.PATH)
class CreateAppointmentPageController(
    private val createAppointment: CreateAppointmentOp,
    private val timeZones: TimeZones
) {

    @GetMapping
    fun getAppointmentPage(
        @RequestParam("dateTime") dateTime: LocalDateTime?
    ): ModelAndView {
        return appointmentPageModelAndView(
            pageMode = EntityPageMode.CREATE,
            allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem)
        ) {
            if (dateTime != null) {
                "dateTime" bindTo dateTime.toString()
            }
        }
    }

    @PostMapping
    fun createAppointment(
        editAppointmentRequest: EditAppointmentRequest,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): Any {
        return try {
            val appointment = createAppointment(therapist.ref, editAppointmentRequest)
            seeOther(calendarForDayWithFocus(editAppointmentRequest.dateTime.toLocalDate(), appointment.id))
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

    companion object {
        const val PATH = "/therapist/appointments/new"
        const val DATE_TIME = "dateTime"
        const val ADD_TO_DATE_TIME_PATH = "/therapist/appointments/new?$DATE_TIME={$DATE_TIME}"
    }

}