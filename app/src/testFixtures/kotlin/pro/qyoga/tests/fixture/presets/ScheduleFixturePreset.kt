package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.backgrounds.AppointmentsBackgrounds
import pro.qyoga.tests.fixture.backgrounds.ClientsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF


data class ScheduleFixture(
    val clients: List<Client>,
    val appointments: List<EditAppointmentRequest>,
    val googleCalendarsFixture: TherapistGoogleCalendarsFixture,
    val therapist: TherapistRef = THE_THERAPIST_REF
) {

    init {
        require(clients.map { it.therapistRef }
            .all { it == therapist }) { "all clients should be assigned to the therapist" }
        require(googleCalendarsFixture.therapist == therapist) { "therapist should be the same as in the fixture" }
    }

    fun theAppointment() =
        appointments.single()

}

@Component
class ScheduleFixturePreset(
    private val clientsBackgrounds: ClientsBackgrounds,
    private val appointmentsBackgrounds: AppointmentsBackgrounds,
    private val googleCalendarFixturePreset: GoogleCalendarsFixturePresets
) {

    fun insertFixture(scheduleFixture: ScheduleFixture) {
        clientsBackgrounds.createClients(scheduleFixture.clients)

        scheduleFixture.appointments.forEach {
            appointmentsBackgrounds.create(it, scheduleFixture.therapist)
        }

        googleCalendarFixturePreset.insertFixture(scheduleFixture.googleCalendarsFixture)
    }

    companion object {

        fun withSingleAppointmentAndEnabledGoogleCalendar(): ScheduleFixture {
            val client = ClientsObjectMother.createClient()

            val appointment = AppointmentsObjectMother.randomEditAppointmentRequest(client.ref())
            val googleCalendarFixture = GoogleCalendarsFixturePresets.withSingleCalendar(shouldBeShown = true)

            return ScheduleFixture(
                listOf(client),
                listOf(appointment),
                googleCalendarFixture
            )
        }

    }

}
