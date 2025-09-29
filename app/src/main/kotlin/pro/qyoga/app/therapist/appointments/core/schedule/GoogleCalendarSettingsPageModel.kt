package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.web.servlet.ModelAndView
import pro.qyoga.i9ns.calendars.google.views.GoogleAccountCalendarsSettingsView

data class GoogleCalendarSettingsPageModel(
    val accounts: List<GoogleAccountCalendarsSettingsView>
) : ModelAndView("therapist/appointments/google-settings-component.html") {

    init {
        addObject("accounts", accounts)
        addObject("hasAccounts", accounts.isNotEmpty())
    }

}
