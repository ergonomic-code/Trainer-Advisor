package pro.qyoga.tests.fixture.object_mothers.calendars.ical

import pro.qyoga.core.calendar.ical.model.ICalCalendar
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.calendars.CalendarsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import java.net.URI


object ICalCalendarsObjectMother {

    fun aICalCalendar(icsFile: String): ICalCalendar {
        return ICalCalendar(
            THE_THERAPIST_REF,
            CalendarsObjectMother.aAppointmentEventTitle(),
            URI.create(faker.internet().url()).toURL(),
            icsFile
        )
    }

}