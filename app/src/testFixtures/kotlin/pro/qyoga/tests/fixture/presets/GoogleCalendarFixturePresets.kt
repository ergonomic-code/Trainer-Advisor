package pro.qyoga.tests.fixture.presets

import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleCalendar
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer


class GoogleCalendarFixturePresets(
    private val mockGoogleOAuthServer: MockGoogleOAuthServer,
    private val mockGoogleCalendar: MockGoogleCalendar,
    private val googleCalendarsService: GoogleCalendarTestApi
) {

    fun setupCalendar(therapistRef: TherapistRef, calendarId: String) {
        val refreshToken = "refreshToken"
        val accessToken = "accessToken"
        mockGoogleOAuthServer.OnRefreshToken(refreshToken).returnsToken(accessToken)
        mockGoogleCalendar.OnGetCalendars(accessToken).returnsCalendars(
            listOf(aGoogleCalendar(ownerRef = therapistRef, externalId = calendarId))
        )
        googleCalendarsService.addAccount(therapistRef, faker.internet().emailAddress(), refreshToken)
    }

}