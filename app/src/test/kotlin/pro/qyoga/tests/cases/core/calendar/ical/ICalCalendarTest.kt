package pro.qyoga.tests.cases.core.calendar.ical

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.shouldBe
import net.fortuna.ical4j.model.component.VEvent
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.calendar.ical.model.ICalCalendar
import pro.qyoga.core.calendar.ical.model.calendarItemsIn
import pro.qyoga.core.calendar.ical.model.findById
import pro.qyoga.core.calendar.ical.model.vEvents
import pro.qyoga.core.calendar.ical.platform.ical4j.id
import pro.qyoga.tests.fixture.object_mothers.calendars.ical.ICalCalendarsObjectMother.aICalCalendar
import java.time.Duration
import java.time.ZonedDateTime


@DisplayName("Модель ical-календаря")
class ICalCalendarTest : FreeSpec({

    "для календаря с повторяющимся событием" - {

        val ical = aICalCalendar(singleWeeklyEvent)

        "при запросе событий за период включающим это событие" - {
            val interval = Interval.of(ZonedDateTime.now(), Duration.ofDays(7))
            val events = ical.calendarItemsIn(interval)

            "должен вернуть одно событие" {
                events.size shouldBe 1
            }

            "событие должно попадать в запрошенный период" {
                events.first().dateTime shouldBeAfter interval.from
                events.first().dateTime shouldBeBefore interval.to
            }
        }

    }

    "для календаря с перенесённым повторяющимся событием" - {

        val ical = aICalCalendar(MovedRecurringEvent.icsFile)
        val theEventId = ical.lastEvent.id

        "при запросе события по id" - {
            val event = ical.findById(theEventId)

            "должен вернуть событие" {
                event shouldBe ical.lastEvent
            }
        }
    }

})

private val ICalCalendar.lastEvent: VEvent
    get() = vEvents().last()
