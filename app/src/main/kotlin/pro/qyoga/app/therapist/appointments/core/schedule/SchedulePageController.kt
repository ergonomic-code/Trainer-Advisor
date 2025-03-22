package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pro.qyoga.core.appointments.core.AppointmentRef
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import java.time.LocalDate
import java.util.*


@Controller
@RequestMapping(SchedulePageController.PATH)
class SchedulePageController(
    private val getCalendarAppointments: GetCalendarAppointmentsOp
) {

    @GetMapping
    fun getCalendarPage(
        @RequestParam(DATE) date: LocalDate = LocalDate.now(),
        @RequestParam(FOCUSED_APPOINTMENT) focusedAppointment: UUID? = null,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): CalendarPageModel {
        val appointments = getCalendarAppointments(therapist.ref, date)
        return CalendarPageModel.of(date, appointments, focusedAppointment)
    }

    companion object {
        const val PATH = "/therapist/schedule"
        const val DATE = "date"
        const val FOCUSED_APPOINTMENT = CalendarPageModel.FOCUSED_APPOINTMENT
        const val DATE_PATH = "$PATH?$DATE={$DATE}"
        const val DATE_APPOINTMENT_PATH = "$PATH?$DATE={$DATE}&$FOCUSED_APPOINTMENT={$FOCUSED_APPOINTMENT}"
        fun calendarForDateUrl(date: LocalDate) = DATE_PATH.replace("{$DATE}", date.toString())
        fun calendarForDayWithFocus(date: LocalDate, appointmentRef: AppointmentRef) = DATE_APPOINTMENT_PATH
            .replace("{$DATE}", date.toString())
            .replace("{$FOCUSED_APPOINTMENT}", appointmentRef.id.toString())
    }

}