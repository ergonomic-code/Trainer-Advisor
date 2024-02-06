package pro.qyoga.app.therapist.appointments.core

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref


enum class ScheduleTabs {
    FUTURE,
    PAST
}

@Controller
@RequestMapping(SchedulePageController.PATH)
class SchedulePageController(
    private val getFutureAppointmentsWorkflow: GetFutureAppointmentsWorkflow,
    private val getPastAppointmentsWorkflow: GetPastAppointmentsWorkflow
) {

    @GetMapping
    fun getFutureSchedulePage(
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        val futureAppointments = getFutureAppointmentsWorkflow(therapist.ref)
        return modelAndView("therapist/appointments/schedule.html") {
            "activeTab" bindTo ScheduleTabs.FUTURE.name.lowercase()
            "futureAppointments" bindTo futureAppointments
        }
    }

    @GetMapping(params = ["past=true"])
    fun getPastSchedulePage(
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        val pastAppointments = getPastAppointmentsWorkflow(therapist.ref)
        return modelAndView("therapist/appointments/schedule.html") {
            "activeTab" bindTo ScheduleTabs.PAST.name.lowercase()
            "pastAppointments" bindTo pastAppointments
        }
    }

    companion object {
        const val PATH = "/therapist/schedule"
    }

}