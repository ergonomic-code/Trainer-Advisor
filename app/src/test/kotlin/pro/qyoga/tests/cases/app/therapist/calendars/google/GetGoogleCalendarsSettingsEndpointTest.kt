package pro.qyoga.tests.cases.app.therapist.calendars.google

import io.kotest.core.annotation.DisplayName
import pro.qyoga.core.calendar.google.GoogleAccountCalendarsView
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import pro.qyoga.tests.pages.therapist.appointments.GoogleCalendarSettingsComponent


@DisplayName("Эндпоинт получения компонента настройки интеграции с Google Calendar")
class GetGoogleCalendarsSettingsEndpointTest : QYogaAppIntegrationBaseKoTest({

    "должен возвращать пустой список аккаунтов для терапевта без настроенной интеграции" {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val accounts = emptyList<GoogleAccountCalendarsView>()

        // Действие
        val res = therapist.appointments.getGoogleCalendarComponent()

        // Проверка
        res shouldHaveComponent GoogleCalendarSettingsComponent(accounts)
    }

})