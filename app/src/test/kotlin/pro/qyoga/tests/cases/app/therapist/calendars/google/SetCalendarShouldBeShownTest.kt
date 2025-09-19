package pro.qyoga.tests.cases.app.therapist.calendars.google

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.calendar.google.GoogleAccountContentView
import pro.qyoga.tests.clients.TherapistClient.Companion.loginAsTheTherapist
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.presets.GoogleCalendarFixturePresets
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
    val googleCalendarFixturePresets =
        GoogleCalendarFixturePresets(mockGoogleOAuthServer, mockGoogleCalendar, googleCalendarsTestApi)

    "должен сохранять заданное значение в БД" {
        // Сетап
        val calendarId = "calendarId"
        val therapist = loginAsTheTherapist()
        val googleAccount = googleCalendarFixturePresets.setupCalendar(calendarId = calendarId)

        // Действие
        therapist.googleCalendarIntegration.setShouldBeShown(googleAccount.ref(), calendarId, true)

        // Проверка
        val settings = googleCalendarsTestApi.getGoogleCalendarsSettings(THE_THERAPIST_REF)
        (settings.single().content as GoogleAccountContentView.Calendars).calendars.single { it.id == calendarId }.shouldBeShown shouldBe true
    }

})