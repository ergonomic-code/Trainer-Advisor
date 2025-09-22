package pro.qyoga.tests.fixture.test_apis

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.appointments.core.schedule.GoogleCalendarSettingsController
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.GoogleAccount
import pro.qyoga.i9ns.calendars.google.GoogleAccountCalendarsView
import pro.qyoga.i9ns.calendars.google.GoogleAccountRef
import pro.qyoga.i9ns.calendars.google.GoogleCalendarsService
import pro.qyoga.tests.fixture.object_mothers.therapists.idOnlyUserDetails


@Component
class GoogleCalendarTestApi(
    private val googleCalendarSettingsController: GoogleCalendarSettingsController,
    private val googleCalendarsService: GoogleCalendarsService
) {

    fun getGoogleCalendarsSettings(therapistRef: TherapistRef): List<GoogleAccountCalendarsView> {
        return googleCalendarSettingsController.getGoogleCalendarSettingsComponent(
            idOnlyUserDetails(therapistRef.id!!)
        ).accounts
    }

    fun addAccount(googleAccount: GoogleAccount): GoogleAccount {
        googleCalendarsService.addGoogleAccount(googleAccount)
        return googleAccount
    }

    fun setShouldBeShown(
        therapistRef: TherapistRef,
        accountRef: GoogleAccountRef,
        calendarId: String,
        shouldBeShown: Boolean
    ) {
        googleCalendarsService.updateCalendarSettings(
            therapistRef,
            accountRef,
            calendarId,
            mapOf("shouldBeShown" to shouldBeShown)
        )
    }

}
