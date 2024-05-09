package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import java.time.LocalDate


@Controller
@RequestMapping(SchedulePageController.PATH)
class SchedulePageController(
    private val getCalendarAppointments: GetCalendarAppointmentsWorkflow
) {

    @GetMapping
    fun getCalendarPage(
        @RequestParam(DATE) date: LocalDate = LocalDate.now(),
        @RequestParam(APPOINTMENT) appointment: Long? = null,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        val appointments = getCalendarAppointments(therapist.ref, date)
        return CalendarPageModel.of(date, appointments, appointment)
    }

    companion object {
        const val PATH = "/therapist/schedule"
        const val DATE = "date"
        const val APPOINTMENT = "appointment"
        const val DATE_PATH = "$PATH?$DATE={$DATE}"
        const val DATE_APPOINTMENT_PATH = "$PATH?$DATE={$DATE}&$APPOINTMENT={$APPOINTMENT}"
        fun calendarForDateUrl(date: LocalDate) = DATE_PATH.replace("{$DATE}", date.toString())
        fun calendarForDayWithFocus(date: LocalDate, appointment: Long) = DATE_APPOINTMENT_PATH
            .replace("{$DATE}", date.toString())
            .replace("{$APPOINTMENT}", appointment.toString())
    }

}