package pro.qyoga.tests.cases.app.therapist.calendars.google

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.i9ns.calendars.google.views.GoogleCalendarsSettingsView
import pro.qyoga.tests.clients.TherapistClient.Companion.loginAsTheTherapist
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.presets.GoogleCalendarsFixturePresets
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import pro.qyoga.tests.infra.wiremock.WireMock


@DisplayName("Эндпоинт установки флага отображения Google-календаря")
class SetCalendarShouldBeShownTest : QYogaAppIntegrationBaseKoTest({

    val googleCalendarsTestApi = getBean<GoogleCalendarTestApi>()
    val mockGoogleOAuthServer = MockGoogleOAuthServer(WireMock.wiremock)
    val mockGoogleCalendar = MockGoogleCalendar(WireMock.wiremock)
    val googleCalendarsFixturePresets =
        GoogleCalendarsFixturePresets(mockGoogleOAuthServer, mockGoogleCalendar, googleCalendarsTestApi)

    "должен сохранять заданное значение в БД" {
        // Сетап
        val therapist = loginAsTheTherapist()
        val singleDisabledCalendarFixture = GoogleCalendarsFixturePresets.withSingleCalendar(shouldBeShown = false)
        val calendarId = singleDisabledCalendarFixture.theCalendar.calendarId
        googleCalendarsFixturePresets.insertFixture(singleDisabledCalendarFixture)

        // Действие
        therapist.googleCalendarIntegration.setShouldBeShown(
            googleAccount = singleDisabledCalendarFixture.theGoogleAccount.ref(),
            calendarId = calendarId,
            shouldBeShown = true
        )

        // Проверка
        val settings = googleCalendarsTestApi.getGoogleCalendarsSettings(THE_THERAPIST_REF)
        (settings.single().content as GoogleCalendarsSettingsView.Calendars).calendars.single { it.id == calendarId }.shouldBeShown shouldBe true
    }

})
