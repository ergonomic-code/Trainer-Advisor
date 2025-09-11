package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class GoogleCalendarSettingsController {

    @GetMapping(PATH)
    fun getGoogleCalendarSettingsComponent(): String {
        return "therapist/appointments/google-settings-component.html"
    }

    companion object {
        const val PATH = "/therapist/schedule/settings/google-calendar"
    }

}