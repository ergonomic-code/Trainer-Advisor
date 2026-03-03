package pro.qyoga.tests.fixture.object_mothers.calendars.ical

import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.component.CalendarComponent
import net.fortuna.ical4j.model.component.Standard
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VTimeZone
import net.fortuna.ical4j.model.property.*
import pro.azhidkov.platform.kotlin.ifNotNull
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendar
import pro.qyoga.i9ns.calendars.ical.model.ICalZonedCalendarItem
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.calendars.CalendarsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import java.io.StringWriter
import java.net.URI
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.Temporal
import net.fortuna.ical4j.model.parameter.TzId as TzIdParam


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

        val vEvent = VEvent()
            .withProperty(Uid(event.id.uid))
            .withProperty(DtStart(event.dateTime.toLocalDateTime()).withParameter(TzIdParam(event.dateTime.zone.id)).fluentTarget)
            .withProperty(Duration(event.duration))
            .withProperty(Summary(event.title))
            .ifNotNull(event.id.recurrenceId) { withProperty(RecurrenceId<Temporal>(it)) }
            .withProperty(Description(event.description))
            .withProperty(Location(event.location))
                as CalendarComponent

        calendar
            .withComponent(vTimeZoneFrom(event.dateTime.zone, event.dateTime))
            .withComponent(vEvent)

        return calendar
    }

    private fun vTimeZoneFrom(zone: ZoneId, reference: ZonedDateTime): VTimeZone {
        val offset = zone.rules.getOffset(reference.toInstant())
        val offsetString = offset.id.replace(":", "")

        val standard = Standard().apply {
            add<Standard>(TzName(offset.id))
            add<Standard>(TzOffsetFrom(offsetString))
            add<Standard>(TzOffsetTo(offsetString))

            val dtStart = DtStart(reference.withYear(1970).toLocalDateTime())
            add<Standard>(dtStart)
        }

        return VTimeZone().apply {
            add<VTimeZone>(TzId(zone.id))
            add<VTimeZone>(standard)
        }
    }

}
