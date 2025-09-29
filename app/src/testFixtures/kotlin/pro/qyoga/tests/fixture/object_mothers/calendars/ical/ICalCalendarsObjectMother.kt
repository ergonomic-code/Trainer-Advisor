package pro.qyoga.tests.fixture.object_mothers.calendars.ical

import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.component.CalendarComponent
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.Location
import net.fortuna.ical4j.model.property.RecurrenceId
import net.fortuna.ical4j.model.property.Uid
import pro.azhidkov.platform.kotlin.ifNotNull
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendar
import pro.qyoga.i9ns.calendars.ical.model.ICalZonedCalendarItem
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.calendars.CalendarsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import java.io.StringWriter
import java.net.URI
import java.time.temporal.Temporal


object ICalCalendarsObjectMother {

    fun aICalCalendar(icsFile: String): ICalCalendar {
        return ICalCalendar(
            THE_THERAPIST_REF,
            CalendarsObjectMother.aAppointmentEventTitle(),
            URI.create(faker.internet().url()).toURL(),
            icsFile
        )
    }

    fun aIcsFile(event: ICalZonedCalendarItem): String {
        val calendar = aICalCalendar(event)
        val icsFile = StringWriter()
        CalendarOutputter().output(calendar, icsFile)
        return icsFile.toString()
    }

    fun aICalCalendar(event: ICalZonedCalendarItem): Calendar {
        val calendar = Calendar()
            .withProdId("-//azhidkov.pro//Trainer Advisor Tests//RU")
            .withDefaults()
            .fluentTarget

        calendar.withComponent(
            VEvent(event.dateTime, event.duration, event.title)
                .withProperty(Uid(event.id.uid))
                .ifNotNull(event.id.recurrenceId) { withProperty(RecurrenceId<Temporal>(it)) }
                .withProperty(Description(event.description))
                .withProperty(Location(event.location))
                    as CalendarComponent

        )
        return calendar
    }

}
