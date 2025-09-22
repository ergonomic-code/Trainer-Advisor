package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendar
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendarItem
import pro.qyoga.tests.fixture.backgrounds.ICalCalendarsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.calendars.ical.ICalCalendarsObjectMother.aIcsFile
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF


@Component
class ICalsCalendarsFixturePresets(
    private val iCalCalendarsBackgrounds: ICalCalendarsBackgrounds
) {

    fun createICalCalendarWithSingleEvent(
        event: ICalCalendarItem,
    ): ICalCalendar {
        val icsFile = aIcsFile(event)

        val iCalCalendar = ICalCalendar(
            THE_THERAPIST_REF,
            "Test ical",
            iCalCalendarsBackgrounds.aIcsUrl(),
            icsFile,
        )

        val ical = iCalCalendarsBackgrounds.createICalCalendar(iCalCalendar)

        return ical
    }

}
