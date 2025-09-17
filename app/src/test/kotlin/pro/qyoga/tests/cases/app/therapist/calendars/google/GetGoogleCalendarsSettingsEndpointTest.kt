package pro.qyoga.tests.cases.app.therapist.calendars.google

import io.kotest.core.annotation.DisplayName
import pro.qyoga.core.calendar.google.GoogleAccountCalendarsView
import pro.qyoga.core.calendar.google.GoogleAccountContentView
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.presets.googleCalendarFixturePresets
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import pro.qyoga.tests.pages.therapist.appointments.GoogleCalendarSettingsComponent
import java.util.*


@DisplayName("Эндпоинт получения компонента настройки интеграции с Google Calendar")
class GetGoogleCalendarsSettingsEndpointTest : QYogaAppIntegrationBaseKoTest({

    val googleCalendarsFixturePresets = context.googleCalendarFixturePresets()

    "должен возвращать пустой список аккаунтов для терапевта без настроенной интеграции" {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val accounts = emptyList<GoogleAccountCalendarsView>()

        // Действие
        val res = therapist.googleCalendarIntegration.getGoogleCalendarComponent()

        // Проверка
        res shouldHaveComponent GoogleCalendarSettingsComponent(accounts)
    }

    "в случае если запрос каленадрей в гугле возвращает ошибку" - {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val accessToken = "accessToken"
        val account = googleCalendarsFixturePresets.setupCalendar(THE_THERAPIST_REF)
        googleCalendarsFixturePresets.mockGoogleCalendar.OnGetCalendars(accessToken)
            .returnsForbidden()
        val accounts = listOf(
            GoogleAccountCalendarsView(
                UUID.fromString(faker.internet().uuid()),
                account.email,
                GoogleAccountContentView.Error
            )
        )

        "при запросе настроек" - {
            // Действие
            val res = therapist.googleCalendarIntegration.getGoogleCalendarComponent()

            "должен корректно вернуть компонент, в котором у аккаунта вместо списка календарей выведена ошибка" {
                // Проверка
                res shouldHaveComponent GoogleCalendarSettingsComponent(accounts)
            }
        }

    }
})