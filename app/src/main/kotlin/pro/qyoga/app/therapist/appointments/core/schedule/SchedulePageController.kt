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
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        val appointments = getCalendarAppointments(therapist.ref, date)
        return CalendarPageModel.of(date, appointments)
    }

    companion object {
        const val PATH = "/therapist/schedule"
        const val DATE = "date"
        const val DATE_PATH = "/therapist/schedule?$DATE={date}"
    }

}