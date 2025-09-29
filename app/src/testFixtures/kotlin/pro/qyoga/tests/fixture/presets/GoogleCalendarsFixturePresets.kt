package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.model.GoogleAccount
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendarItem
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendarSettings
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aCalendarName
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleAccount
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleCalendar
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleToken
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.GoogleAccessToken
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer


data class TherapistGoogleCalendarsFixture(
    val accounts: List<Pair<GoogleAccount, GoogleAccessToken>>,
    val calendarsSettings: List<GoogleCalendarSettings>,
    val events: List<GoogleCalendarItem<*>>,
    val therapist: TherapistRef = THE_THERAPIST_REF
) {

    init {
        require(accounts.map { it.first.ownerRef }
            .all { it == therapist }) { "all google accounts should be assigned to the therapist" }
        require(
            calendarsSettings.map(GoogleCalendarSettings::ownerRef)
                .all { it == therapist }) { "all google calendars should be assigned to the therapist" }
    }

    val calendars = calendarsSettings.map {
        aGoogleCalendar(it.ownerRef, it.calendarId, aCalendarName())
    }

    val theGoogleAccount: GoogleAccount = accounts.single().first

    val theCalendar = calendarsSettings.single()

}

@Component
class GoogleCalendarsFixturePresets(
    val mockGoogleOAuthServer: MockGoogleOAuthServer,
    val mockGoogleCalendar: MockGoogleCalendar,
    val googleCalendarsTestApi: GoogleCalendarTestApi
) {

    fun insertFixture(googleCalendarsFixture: TherapistGoogleCalendarsFixture) {
        googleCalendarsFixture.accounts.forEach { (acc, accessToken) ->
            googleCalendarsTestApi.addAccount(acc)
            mockGoogleOAuthServer.OnRefreshToken(acc.refreshToken.show()).returnsToken(accessToken)
            val accCalendars = googleCalendarsFixture.calendars.filter { it.ownerRef == acc.ownerRef }
            mockGoogleCalendar.OnGetCalendars(accessToken)
                .returnsCalendars(accCalendars)

            accCalendars.forEach { calendar ->
                val events = googleCalendarsFixture.events.filter { it.id.calendarId == calendar.externalId }

                mockGoogleCalendar.OnGetEvents(accessToken, calendar.externalId)
                    .returnsEvents(*events.toTypedArray())

                events.forEach { event ->
                    mockGoogleCalendar.OnGetEventById(accessToken, event.id)
                        .returnsEvent(event)
                }
            }
        }
        googleCalendarsFixture.calendarsSettings
            .filter { it.shouldBeShown }
            .forEach {
                googleCalendarsTestApi.setShouldBeShown(
                    googleCalendarsFixture.therapist,
                    it.googleAccountRef,
                    it.calendarId,
                    true
                )
            }
    }

    fun setFailureOnRequestForEvents(googleCalendarsFixture: TherapistGoogleCalendarsFixture) =
        googleCalendarsFixture.accounts.forEach { (acc, accessToken) ->
            mockGoogleCalendar.OnGetEvents(accessToken, googleCalendarsFixture.theCalendar.calendarId)
                .returnsForbidden()
        }

    fun setFailureOnRequestForCalendars(googleCalendarsFixture: TherapistGoogleCalendarsFixture) =
        googleCalendarsFixture.accounts.forEach { (acc, accessToken) ->
            mockGoogleCalendar.OnGetCalendars(accessToken)
                .returnsForbidden()
        }

    companion object {

        fun withSingleCalendar(
            shouldBeShown: Boolean = false
        ): TherapistGoogleCalendarsFixture {
            val googleAccount = aGoogleAccount(therapist = THE_THERAPIST_REF)
            val googleCalendar = GoogleCalendarObjectMother.aGoogleCalendarSettings(
                googleAccountRef = googleAccount.ref(),
                shouldBeShown = shouldBeShown
            )

            return TherapistGoogleCalendarsFixture(
                accounts = listOf(googleAccount to aGoogleToken()),
                calendarsSettings = listOf(googleCalendar),
                events = emptyList(),
                therapist = THE_THERAPIST_REF
            )
        }

        fun withSingleCalendarAndEvent(
            event: GoogleCalendarItem<*>,
            shouldBeShown: Boolean = true
        ): TherapistGoogleCalendarsFixture {
            val googleAccount = aGoogleAccount(therapist = THE_THERAPIST_REF)
            val googleCalendar = GoogleCalendarObjectMother.aGoogleCalendarSettings(
                externalId = event.id.calendarId,
                googleAccountRef = googleAccount.ref(),
                shouldBeShown = shouldBeShown
            )

            return TherapistGoogleCalendarsFixture(
                accounts = listOf(googleAccount to aGoogleToken()),
                calendarsSettings = listOf(googleCalendar),
                events = listOf(event),
                therapist = THE_THERAPIST_REF
            )
        }

    }

}
