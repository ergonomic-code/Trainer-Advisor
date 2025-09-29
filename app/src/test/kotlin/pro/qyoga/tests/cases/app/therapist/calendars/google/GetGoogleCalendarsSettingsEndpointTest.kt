package pro.qyoga.tests.cases.app.therapist.calendars.google

import io.kotest.core.annotation.DisplayName
import pro.qyoga.i9ns.calendars.google.views.GoogleAccountCalendarsSettingsView
import pro.qyoga.i9ns.calendars.google.views.GoogleCalendarSettingsView
import pro.qyoga.i9ns.calendars.google.views.GoogleCalendarsSettingsView
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.presets.GoogleCalendarsFixturePresets
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import pro.qyoga.tests.pages.therapist.appointments.google_calendars.GoogleCalendarsSettingsComponent
import pro.qyoga.tests.platform.spring.context.getBean
import java.util.*


@DisplayName("Эндпоинт получения компонента настройки интеграции с Google Calendar")
class GetGoogleCalendarsSettingsEndpointTest : QYogaAppIntegrationBaseKoTest({

    val googleCalendarsFixturePresets = context.getBean<GoogleCalendarsFixturePresets>()

    "должен возвращать пустой список аккаунтов для терапевта без настроенной интеграции" {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val accounts = emptyList<GoogleAccountCalendarsSettingsView>()

        // Действие
        val res = therapist.googleCalendarIntegration.getGoogleCalendarComponent()

        // Проверка
        res shouldHaveComponent GoogleCalendarsSettingsComponent(accounts)
    }

    "должен корректно рендерить компонент настройки интеграции с Google Calendar с календарями" - {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val twoCalendarsFixture = GoogleCalendarsFixturePresets.withTwoCalendars(
            shouldBeShown = listOf(true, false)
        )
        googleCalendarsFixturePresets.insertFixture(twoCalendarsFixture)

        val expectedCalendarsSettings =
            GoogleCalendarsSettingsView.Calendars(twoCalendarsFixture.calendarsAndSettings.map { (cal, settings) ->
                GoogleCalendarSettingsView(
                    cal.externalId,
                    cal.name,
                    settings.shouldBeShown
                )
            })
        val expectedAccounts = listOf(
            GoogleAccountCalendarsSettingsView(
                UUID.fromString(faker.internet().uuid()),
                twoCalendarsFixture.theGoogleAccount.email,
                expectedCalendarsSettings
            )
        )

        // Действие
        val res = therapist.googleCalendarIntegration.getGoogleCalendarComponent()

        "должен корректно вернуть компонент, в котором у аккаунта вместо списка календарей выведена ошибка" {
            // Проверка
            res shouldHaveComponent GoogleCalendarsSettingsComponent(expectedAccounts)
        }
    }

    "в случае если запрос календарей в гугле возвращает ошибку" - {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val singleCalendarFixture = GoogleCalendarsFixturePresets.withSingleCalendar()
        googleCalendarsFixturePresets.insertFixture(singleCalendarFixture)
        googleCalendarsFixturePresets.setFailureOnRequestForCalendars(singleCalendarFixture)

        val expectedAccounts = listOf(
            GoogleAccountCalendarsSettingsView(
                UUID.fromString(faker.internet().uuid()),
                singleCalendarFixture.theGoogleAccount.email,
                GoogleCalendarsSettingsView.Error
            )
        )

        // Действие
        val res = therapist.googleCalendarIntegration.getGoogleCalendarComponent()

        "должен корректно вернуть компонент, в котором у аккаунта вместо списка календарей выведена ошибка" {
            // Проверка
            res shouldHaveComponent GoogleCalendarsSettingsComponent(expectedAccounts)
        }

    }
})
