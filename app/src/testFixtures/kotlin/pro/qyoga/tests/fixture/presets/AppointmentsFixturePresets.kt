package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.tests.fixture.backgrounds.AppointmentsBackgrounds
import pro.qyoga.tests.fixture.backgrounds.ClientsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.randomEditAppointmentRequest
import pro.qyoga.tests.fixture.object_mothers.calendars.CalendarsObjectMother.aCalendarItem
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF


@Component
class AppointmentsFixturePresets(
    private val appointmentsBackgrounds: AppointmentsBackgrounds,
    private val clientBackgrounds: ClientsBackgrounds,
    private val calendarsFixturePresets: CalendarsFixturePresets
) {

    fun createAppointmentFromIcsEvent(): Appointment {
        val client = clientBackgrounds.aClient()
        val icsEvent = aCalendarItem()
        calendarsFixturePresets.createICalCalendarWithSingleEvent(icsEvent)
        val app = appointmentsBackgrounds.create(
            randomEditAppointmentRequest(
                client = client.ref(),
                dateTime = icsEvent.dateTime.toLocalDateTime(),
                timeZone = icsEvent.dateTime.zone,
            ),
            THE_THERAPIST_REF
        )

        return app
    }

}