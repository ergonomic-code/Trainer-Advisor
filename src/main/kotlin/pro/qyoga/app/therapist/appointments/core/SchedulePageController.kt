package pro.qyoga.app.therapist.appointments.core

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView


@Controller
@RequestMapping(SchedulePageController.PATH)
class SchedulePageController {

    @GetMapping
    fun getSchedulePage(): ModelAndView {
        return modelAndView("therapist/appointments/schedule.html") {}
    }

    companion object {
        const val PATH = "/therapist/schedule"
    }

}