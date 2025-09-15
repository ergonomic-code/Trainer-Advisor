package pro.qyoga.tests.fixture.presets

import pro.qyoga.core.calendar.google.GoogleAccount
import pro.qyoga.core.calendar.google.GoogleCalendarItem
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleCalendar
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer


class GoogleCalendarFixturePresets(
    private val mockGoogleOAuthServer: MockGoogleOAuthServer,
    private val mockGoogleCalendar: MockGoogleCalendar,
    private val googleCalendarsTestApi: GoogleCalendarTestApi
) {

    fun setupCalendar(
        therapistRef: TherapistRef,
        calendarId: String,
        vararg events: GoogleCalendarItem,
        shouldBeShown: Boolean = false,
    ): GoogleAccount {
        val refreshToken = "refreshToken"
        val accessToken = "accessToken"
        mockGoogleOAuthServer.OnRefreshToken(refreshToken).returnsToken(accessToken)
        mockGoogleCalendar.OnGetCalendars(accessToken).returnsCalendars(
            listOf(aGoogleCalendar(ownerRef = therapistRef, externalId = calendarId))
        )
        mockGoogleCalendar.OnGetEvents(accessToken, calendarId).returnsEvents(*events)
        val account = googleCalendarsTestApi.addAccount(therapistRef, faker.internet().emailAddress(), refreshToken)
        googleCalendarsTestApi.setShouldBeShown(therapistRef, account, calendarId, shouldBeShown)
        return account
    }

}