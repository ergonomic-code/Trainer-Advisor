package pro.qyoga.tests.fixture.test_apis

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.appointments.core.schedule.GoogleCalendarSettingsController
import pro.qyoga.core.calendar.google.GoogleAccount
import pro.qyoga.core.calendar.google.GoogleAccountCalendarsView
import pro.qyoga.core.calendar.google.GoogleCalendarsService
import pro.qyoga.core.users.therapists.TherapistRef
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

    fun addAccount(therapistRef: TherapistRef, email: String, refreshToken: String) {
        googleCalendarsService.addGoogleAccount(GoogleAccount(therapistRef, email, refreshToken))
    }

}