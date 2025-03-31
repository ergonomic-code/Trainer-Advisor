package pro.qyoga.tests.fixture.presets

import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.component.CalendarComponent
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.RecurrenceId
import net.fortuna.ical4j.model.property.Uid
import org.springframework.stereotype.Component
import pro.azhidkov.platform.kotlin.ifNotNull
import pro.qyoga.core.calendar.ical.model.ICalCalendar
import pro.qyoga.core.calendar.ical.model.ICalCalendarItem
import pro.qyoga.tests.fixture.backgrounds.ICalCalendarsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import java.io.StringWriter
import java.time.temporal.Temporal


@Component
class CalendarsFixturePresets(
    private val iCalCalendarsBackgrounds: ICalCalendarsBackgrounds
) {

    fun createICalCalendarWithSingleEvent(
        event: ICalCalendarItem,
    ): ICalCalendar {
        val calendar = Calendar()
            .withProdId("-//azhidkov.pro//Trainer Advisor Tests//RU")
            .withDefaults()
            .fluentTarget

        calendar.withComponent(
            VEvent(event.dateTime, event.duration, event.title)
                .withProperty(Uid(event.id.uid))
                .ifNotNull(event.id.recurrenceId) { withProperty(RecurrenceId<Temporal>(it)) }
                .withProperty(Description(event.description)) as CalendarComponent
        )
        val icsFile = StringWriter()
        CalendarOutputter().output(calendar, icsFile)

        val iCalCalendar = ICalCalendar(
            THE_THERAPIST_REF,
            "Test ical",
            iCalCalendarsBackgrounds.aIcsUrl(),
            icsFile.toString(),
        )

        val ical = iCalCalendarsBackgrounds.createICalCalendar(iCalCalendar)

        return ical
    }

}
