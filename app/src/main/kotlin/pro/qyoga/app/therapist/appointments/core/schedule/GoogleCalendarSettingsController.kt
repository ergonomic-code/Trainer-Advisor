package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.calendar.google.GoogleAccountCalendars
import pro.qyoga.core.calendar.google.GoogleCalendarsService
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref

data class GoogleCalendarSettingsPageModel(
    val accounts: List<GoogleAccountCalendars>
) : ModelAndView("therapist/appointments/google-settings-component.html") {

    init {
        addObject("accounts", accounts)
    }

}

@Controller
class GoogleCalendarSettingsController(
    private val googleCalendarsService: GoogleCalendarsService
) {

    @GetMapping(PATH)
    fun getGoogleCalendarSettingsComponent(
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): GoogleCalendarSettingsPageModel {
        val googleAccounts = googleCalendarsService.findGoogleAccountCalendars(therapist.ref)
        return GoogleCalendarSettingsPageModel(googleAccounts)
    }

    companion object {
        const val PATH = "/therapist/schedule/settings/google-calendar"
    }

}