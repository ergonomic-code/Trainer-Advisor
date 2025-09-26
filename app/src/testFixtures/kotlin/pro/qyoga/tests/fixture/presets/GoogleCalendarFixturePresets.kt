package pro.qyoga.tests.fixture.presets

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.GoogleAccount
import pro.qyoga.i9ns.calendars.google.GoogleCalendarItem
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aCalendarName
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleCalendar
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer
import pro.qyoga.tests.infra.wiremock.WireMock
import pro.qyoga.tests.platform.spring.context.getBean


@Component
class GoogleCalendarFixturePresets(
    val mockGoogleOAuthServer: MockGoogleOAuthServer,
    val mockGoogleCalendar: MockGoogleCalendar,
    val googleCalendarsTestApi: GoogleCalendarTestApi
) {

    fun setupCalendar(
        vararg events: GoogleCalendarItem<*>,
        therapistRef: TherapistRef = THE_THERAPIST_REF,
        calendarId: String = events.firstOrNull()?.id?.calendarId ?: aCalendarName(),
        shouldBeShown: Boolean = false,
        accessToken: String = "accessToken"
    ): GoogleAccount {
        check(events.map { it.id.calendarId }.all { it == calendarId }) { "events should have the same calendarId" }
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

        events.forEach {
            mockGoogleCalendar.OnGetEventById(accessToken, it.id).returnsEvent(it)
        }

        googleCalendarsTestApi.setShouldBeShown(therapistRef, account.ref(), calendarId, shouldBeShown)
        return account
    }

}

fun ApplicationContext.googleCalendarFixturePresets() = GoogleCalendarFixturePresets(
    MockGoogleOAuthServer(WireMock.wiremock),
    MockGoogleCalendar(WireMock.wiremock),
    getBean()
)
