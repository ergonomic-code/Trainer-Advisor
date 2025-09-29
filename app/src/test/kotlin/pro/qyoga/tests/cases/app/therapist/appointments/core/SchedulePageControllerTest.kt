package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.collections.shouldHaveSize
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.data.randomWorkingTime
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleCalendarItem
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.fixture.presets.AppointmentsFixturePresets
import pro.qyoga.tests.fixture.presets.GoogleCalendarsFixturePresets
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import java.time.LocalDate


@DisplayName("Контроллер страницы календаря")
class SchedulePageControllerTest : QYogaAppIntegrationBaseKoTest({

    val appointmentsFixturePresets = getBean<AppointmentsFixturePresets>()
    val googleCalendarsFixturePresets = getBean<GoogleCalendarsFixturePresets>()

    val schedulePageController = getBean<SchedulePageController>()

    "при наличии приёма, созданного на базе события ics-календаря" - {
        // Сетап
        val app = appointmentsFixturePresets.createAppointmentFromIcsEvent()

        // Действие
        val calendarPageModel =
            schedulePageController.getCalendarPage(app.wallClockDateTime.toLocalDate(), null, theTherapistUserDetails)

        // Проверка
        "должен возвращать карточку только для приёма" {
            calendarPageModel.appointmentCards() shouldHaveSize 1
            calendarPageModel.appointmentCards().first() shouldMatch app
        }
    }

    "если есть подключенный, но не настроенный Google-аккаунт с календарём с событием 14 сентября 25 года, то при запросе расписания за 13-15 сентября метод должен не включать это событие" {
        // Сетап
        val date = LocalDate.of(2025, 9, 14)
        val event = aGoogleCalendarItem(
            date = { date.atTime(randomWorkingTime()) }
        )
        val singleEventFixture = GoogleCalendarsFixturePresets.withSingleCalendarAndEvent(
            event,
            shouldBeShown = false
        )
        googleCalendarsFixturePresets.insertFixture(singleEventFixture)

        // Действие
        val calendarPageModel =
            schedulePageController.getCalendarPage(date, null, theTherapistUserDetails)

        // Проверка
        calendarPageModel.appointmentCards() shouldHaveSize 0
    }

    "если есть подключенный и настроенный Google-аккаунт с календарём с событием 14 сентября 25 года, то при запросе расписания за 13-15 сентября метод должен включать это событие" {
        // Сетап
        val date = LocalDate.of(2025, 9, 14)
        val event = aGoogleCalendarItem(
            date = { date.atTime(randomWorkingTime()) },
        )
        val singleEventFixture = GoogleCalendarsFixturePresets.withSingleCalendarAndEvent(event)
        googleCalendarsFixturePresets.insertFixture(singleEventFixture)

        // Действие
        val calendarPageModel =
            schedulePageController.getCalendarPage(date, null, theTherapistUserDetails)

        // Проверка
        calendarPageModel.appointmentCards() shouldHaveSize 1
    }

})
