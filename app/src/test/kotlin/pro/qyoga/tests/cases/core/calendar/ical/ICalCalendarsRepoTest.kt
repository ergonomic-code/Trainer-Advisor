package pro.qyoga.tests.cases.core.calendar.ical

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import pro.qyoga.core.calendar.ical.ICalCalendarsRepo
import pro.qyoga.core.calendar.ical.model.ICalCalendarItem
import pro.qyoga.tests.fixture.backgrounds.ICalCalendarsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.calendars.CalendarsObjectMother.aCalendarItem
import pro.qyoga.tests.fixture.object_mothers.calendars.ical.ICalCalendarsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.presets.CalendarsFixturePresets
import pro.qyoga.tests.infra.web.QYogaAppBaseKoTest
import pro.qyoga.tests.platform.instancio.KSelect.Companion.field


@DisplayName("Репозиторий ical-календарей")
class ICalCalendarsRepoTest : QYogaAppBaseKoTest({

    "операция синхронизации календарей" - {

        val iCalCalendarsRepo = getBean<ICalCalendarsRepo>()

        "при изменении состояния календаря в источнике" - {
            // Сетап
            val iCalEvent = aCalendarItem()
            val iCal = getBean<CalendarsFixturePresets>().createICalCalendarWithSingleEvent(iCalEvent)
            val updatedEvent = aCalendarItem {
                set(field(ICalCalendarItem::id), iCalEvent.id)
            }
            getBean<ICalCalendarsBackgrounds>().updateICalSource(
                iCal.icsUrl,
                ICalCalendarsObjectMother.aIcsFile(updatedEvent)
            )

            // Когда
            iCalCalendarsRepo.sync()

            // И когда
            val actualEvent = iCalCalendarsRepo.findById(THE_THERAPIST_REF, iCalEvent.id)

            // Тогда
            "должна обновлять календари в репозитории" {
                actualEvent shouldBe updatedEvent
            }
        }
    }

})