package pro.qyoga.tests.cases.i9ns.calendar.google

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.calendar.google.GoogleCalendarsService
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleCalendarItem
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.presets.googleCalendarFixturePresets
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime


@DisplayName("Сервис Google календарей")
class GoogleCalendarsServiceTest : QYogaAppIntegrationBaseKoTest({

    val googleCalendarsService = getBean<GoogleCalendarsService>()
    val googleCalendarFixturePresets = context.googleCalendarFixturePresets()

    "метод получения событий в интервале" - {

        "должен возвращать события приведённые к таймзоне запрошенного интервала" {
            // Сетап
            googleCalendarFixturePresets.setupCalendar(
                THE_THERAPIST_REF, GoogleCalendarObjectMother.aCalendarName(),
                aGoogleCalendarItem(
                    date = { ZonedDateTime.of(2025, 9, 16, 6, 0, 0, 0, ZoneId.of("Europe/Moscow")) },
                    duration = Duration.ofMinutes(60)
                ),
                shouldBeShown = true
            )

            // Действие
            val items = googleCalendarsService.findCalendarItemsInInterval(
                THE_THERAPIST_REF,
                Interval.of(ZonedDateTime.of(2025, 9, 16, 0, 0, 0, 0, asiaNovosibirskTimeZone), Duration.ofDays(1))
            )

            // Проверка
            items shouldHaveSize 1
            items.first().dateTime shouldBe LocalDateTime.of(2025, 9, 16, 10, 0, 0, 0)
            items.first().endDateTime shouldBe LocalDateTime.of(2025, 9, 16, 11, 0, 0, 0)
        }
    }

})