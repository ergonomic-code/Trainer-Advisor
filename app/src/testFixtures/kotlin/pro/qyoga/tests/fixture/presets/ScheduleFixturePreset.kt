package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.clients.cards.Client
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.ClientId
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.GoogleAccount
import pro.qyoga.i9ns.calendars.google.GoogleAccountId
import pro.qyoga.i9ns.calendars.google.GoogleAccountRef
import pro.qyoga.i9ns.calendars.google.GoogleCalendarSettings
import pro.qyoga.tests.fixture.backgrounds.AppointmentsBackgrounds
import pro.qyoga.tests.fixture.backgrounds.ClientsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleToken
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDtoMinimal
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer


typealias GoogleAccessToken = String

data class ScheduleFixture(
    val clients: Map<ClientId, ClientCardDto>,
    val appointments: Map<ClientId, List<EditAppointmentRequest>>,
    val googleAccounts: List<Pair<GoogleAccount, GoogleAccessToken>>,
    val googleCalendars: Map<GoogleAccountId, List<GoogleCalendarSettings>>,
    val therapist: TherapistRef = THE_THERAPIST_REF
) {

    fun theAppointment() =
        appointments.values.single().single()

}

@Component
class ScheduleFixturePreset(
    private val clientsBackgrounds: ClientsBackgrounds,
    private val appointmentsBackgrounds: AppointmentsBackgrounds,
    private val googlCalendarTestApi: GoogleCalendarTestApi,
    private val mockGoogleOAuthServer: MockGoogleOAuthServer,
    private val mockGoogleCalendar: MockGoogleCalendar
) {

    fun insertFixture(scheduleFixture: ScheduleFixture) {
        val idMapping = clientsBackgrounds.createClients(scheduleFixture.clients.values, scheduleFixture.therapist.id!!)
            .zip(scheduleFixture.clients.keys)
            .associate { it.second to it.first.ref() }

        scheduleFixture.appointments.forEach { (clientId, appointments) ->
            appointments.forEach {
                appointmentsBackgrounds.create(it.copy(client = idMapping[it.client.id]!!), scheduleFixture.therapist)
            }
        }
        scheduleFixture.googleAccounts.forEach { (acc, accessToken) ->
            googlCalendarTestApi.addAccount(acc)
            mockGoogleOAuthServer.OnRefreshToken(acc.refreshToken.show()).returnsToken()
            mockGoogleCalendar.OnGetCalendars(accessToken)
        }
        scheduleFixture.googleCalendars
            .forEach { (accountId, calendars) ->
                calendars
                    .filter { it.shouldBeShown }
                    .forEach {
                        googlCalendarTestApi.setShouldBeShown(
                            scheduleFixture.therapist,
                            GoogleAccountRef.to(accountId),
                            it.calendarId,
                            true
                        )
                    }
            }
    }

    companion object {

        fun fixtureWithAppointmentAndGoogleCalendar(): ScheduleFixture {
            val client = createClientCardDtoMinimal()
            val client1 = Client(THE_THERAPIST_ID, client)
            val clientId = client1.id

            val appointment = AppointmentsObjectMother.randomEditAppointmentRequest(client1.ref())

            val googleAccount = GoogleCalendarObjectMother.aGoogleAccount(THE_THERAPIST_REF)
            val googleCalendar = GoogleCalendarObjectMother.aGoogleCalendarSettings(
                googleAccountRef = googleAccount.ref(),
                shouldBeShown = true
            )

            return ScheduleFixture(
                mapOf(clientId to client),
                mapOf(clientId to listOf(appointment)),
                listOf(googleAccount to aGoogleToken()),
                mapOf(googleAccount.id to listOf(googleCalendar))
            )
        }

    }

}
