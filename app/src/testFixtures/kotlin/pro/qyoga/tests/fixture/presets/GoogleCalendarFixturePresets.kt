package pro.qyoga.tests.fixture.presets

import org.springframework.context.ApplicationContext
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.calendar.google.GoogleAccount
import pro.qyoga.core.calendar.google.GoogleCalendarItem
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aCalendarName
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleCalendar
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer
import pro.qyoga.tests.infra.wiremock.WireMock
import pro.qyoga.tests.platform.spring.context.getBean


class GoogleCalendarFixturePresets(
    val mockGoogleOAuthServer: MockGoogleOAuthServer,
    val mockGoogleCalendar: MockGoogleCalendar,
    val googleCalendarsTestApi: GoogleCalendarTestApi
) {

    fun setupCalendar(
        therapistRef: TherapistRef,
        calendarId: String = aCalendarName(),
        vararg events: GoogleCalendarItem<*>,
        shouldBeShown: Boolean = false,
        accessToken: String = "accessToken"
    ): GoogleAccount {
        val refreshToken = "refreshToken"
        mockGoogleOAuthServer.OnRefreshToken(refreshToken).returnsToken(accessToken)
        mockGoogleCalendar.OnGetCalendars(accessToken).returnsCalendars(
            listOf(aGoogleCalendar(ownerRef = therapistRef, externalId = calendarId))
        )
        mockGoogleCalendar.OnGetEvents(accessToken, calendarId).returnsEvents(*events)
        val account = googleCalendarsTestApi.addAccount(
            GoogleAccount(
                therapistRef,
                faker.internet().emailAddress(),
                refreshToken
            )
        )
        googleCalendarsTestApi.setShouldBeShown(therapistRef, account.ref(), calendarId, shouldBeShown)
        return account
    }

}

fun ApplicationContext.googleCalendarFixturePresets() = GoogleCalendarFixturePresets(
    MockGoogleOAuthServer(WireMock.wiremock),
    MockGoogleCalendar(WireMock.wiremock),
    getBean()
)