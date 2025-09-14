package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.calendar.google.GoogleAccountCalendarsView
import pro.qyoga.core.calendar.google.GoogleCalendarsService
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref

data class GoogleCalendarSettingsPageModel(
    val accounts: List<GoogleAccountCalendarsView>
) : ModelAndView("therapist/appointments/google-settings-component.html") {

    init {
        addObject("accounts", accounts)
        addObject("hasAccounts", accounts.isNotEmpty())
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

    @PatchMapping(UPDATE_CALENDAR_SETTINGS_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateCalendarSettings(
        @PathVariable calendarId: String,
        @RequestBody settingsPatch: Map<String, Any>,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        googleCalendarsService.updateCalendarSettings(therapist.ref, calendarId, settingsPatch)
        return getGoogleCalendarSettingsComponent(therapist)
    }

    companion object {

        const val PATH = "/therapist/schedule/settings/google-calendar"

        const val UPDATE_CALENDAR_SETTINGS_PATH = "$PATH/calendars/{calendarId}"

        fun updateCalendarSettingsPath(calendarId: String): String =
            UPDATE_CALENDAR_SETTINGS_PATH.replace("{calendarId}", calendarId)

    }

}