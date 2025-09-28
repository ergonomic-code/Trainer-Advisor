package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.model.GoogleAccount
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendarSettings
import pro.qyoga.tests.fixture.backgrounds.AppointmentsBackgrounds
import pro.qyoga.tests.fixture.backgrounds.ClientsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleAccount
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleToken
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.GoogleAccessToken
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer


data class ScheduleFixture(
    val clients: List<Client>,
    val appointments: List<EditAppointmentRequest>,
    val googleAccounts: List<Pair<GoogleAccount, GoogleAccessToken>>,
    val googleCalendars: List<GoogleCalendarSettings>,
    val therapist: TherapistRef = THE_THERAPIST_REF
) {

    fun theAppointment() =
        appointments.single()

}

@Component
class ScheduleFixturePreset(
    private val clientsBackgrounds: ClientsBackgrounds,
    private val appointmentsBackgrounds: AppointmentsBackgrounds,
    private val googleCalendarTestApi: GoogleCalendarTestApi,
    private val mockGoogleOAuthServer: MockGoogleOAuthServer,
    private val mockGoogleCalendar: MockGoogleCalendar
) {

    fun insertFixture(scheduleFixture: ScheduleFixture) {
        clientsBackgrounds.createClients(scheduleFixture.clients)

        scheduleFixture.appointments.forEach {
            appointmentsBackgrounds.create(it, scheduleFixture.therapist)
        }
        scheduleFixture.googleAccounts.forEach { (acc, accessToken) ->
            googleCalendarTestApi.addAccount(acc)
            mockGoogleOAuthServer.OnRefreshToken(acc.refreshToken.show()).returnsToken()
            mockGoogleCalendar.OnGetCalendars(accessToken)
        }
        scheduleFixture.googleCalendars
            .filter { it.shouldBeShown }
            .forEach {
                googleCalendarTestApi.setShouldBeShown(
                    scheduleFixture.therapist,
                    it.googleAccountRef,
                    it.calendarId,
                    true
                )
            }
    }

    companion object {

        fun fixtureWithAppointmentAndGoogleCalendar(): ScheduleFixture {
            val client = ClientsObjectMother.createClient()

            val appointment = AppointmentsObjectMother.randomEditAppointmentRequest(client.ref())

            val googleAccount = aGoogleAccount(therapist = THE_THERAPIST_REF)
            val googleCalendar = GoogleCalendarObjectMother.aGoogleCalendarSettings(
                googleAccountRef = googleAccount.ref(),
                shouldBeShown = true
            )

            return ScheduleFixture(
                listOf(client),
                listOf(appointment),
                listOf(googleAccount to aGoogleToken()),
                listOf(googleCalendar)
            )
        }

    }

}
