package pro.qyoga.tests.cases.app.therapist.calendars.google

import io.kotest.core.annotation.DisplayName
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import pro.qyoga.tests.pages.therapist.appointments.GoogleCalendarSettingsComponent


@DisplayName("UI-компонент настройки интеграции с Google Calendar")
class GoogleCalendarsSettingsTest : QYogaAppIntegrationBaseKoTest({

    "должен корректно рендерится для терапевта без настроенной интеграции" {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val res = therapist.appointments.getGoogleCalendarComponent()

        res shouldHaveComponent GoogleCalendarSettingsComponent
    }

})