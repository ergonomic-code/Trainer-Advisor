package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.collections.shouldHaveSize
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.fixture.presets.AppointmentsFixturePresets
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest


@DisplayName("Контроллер страницы календаря")
class SchedulePageControllerTest : QYogaAppIntegrationBaseKoTest({

    val appointmentsFixturePresets = getBean<AppointmentsFixturePresets>()
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

})