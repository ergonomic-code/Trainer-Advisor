package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import pro.qyoga.i9ns.calendars.google.GoogleCalendarsService
import pro.qyoga.i9ns.calendars.google.model.GoogleAccountRef

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
        @PathVariable googleAccount: GoogleAccountRef,
        @PathVariable calendarId: String,
        @RequestBody settingsPatch: Map<String, Any>,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        googleCalendarsService.updateCalendarSettings(therapist.ref, googleAccount, calendarId, settingsPatch)
        return getGoogleCalendarSettingsComponent(therapist)
    }

    companion object {

        const val PATH = "/therapist/schedule/settings/google-calendar"

        const val UPDATE_CALENDAR_SETTINGS_PATH = "$PATH/{googleAccount}/calendars/{calendarId}"

        fun updateCalendarSettingsPath(googleAccount: GoogleAccountRef, calendarId: String): String =
            UPDATE_CALENDAR_SETTINGS_PATH
                .replace("{googleAccount}", googleAccount.id?.toString() ?: "")
                .replace("{calendarId}", calendarId)

    }

}
