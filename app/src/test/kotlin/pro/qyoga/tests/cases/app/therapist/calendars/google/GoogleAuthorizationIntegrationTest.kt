package pro.qyoga.tests.cases.app.therapist.calendars.google

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import org.springframework.core.env.get
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.app.therapist.oauth2.GoogleOAuthController
import pro.qyoga.i9ns.calendars.google.GoogleCalendarsService
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendar
import pro.qyoga.i9ns.calendars.google.views.GoogleCalendarsSettingsView
import pro.qyoga.tests.assertions.shouldBeRedirectToGoogleOAuth
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.oauth.OAuthObjectMother
import pro.qyoga.tests.fixture.oauth.OAuthObjectMother.aOAuth2AuthorizationResponse
import pro.qyoga.tests.fixture.object_mothers.calendars.google.GoogleCalendarObjectMother.aGoogleAccount
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.GoogleCalendarTestApi
import pro.qyoga.tests.fixture.wiremocks.MockGoogleCalendar
import pro.qyoga.tests.fixture.wiremocks.MockGoogleOAuthServer
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import pro.qyoga.tests.infra.wiremock.WireMock
import pro.qyoga.tests.platform.spring.web_test_client.redirectLocation


@DisplayName("Интеграция с Google OAuth")
class GoogleAuthorizationIntegrationTest : QYogaAppIntegrationBaseKoTest({

    val therapist by lazy { TherapistClient.loginAsTheTherapist() }
    val clientId = context.environment["spring.security.oauth2.client.registration.google.client-id"]!!
    val clientSecret = context.environment["spring.security.oauth2.client.registration.google.client-secret"]!!
    val googleCalendarsService = getBean<GoogleCalendarsService>()
    val googleCalendarsTestApi = getBean<GoogleCalendarTestApi>()

    "Spring Security" - {
        "при запросе на авторизацию в Google" - {
            // Сетап

            // Действие
            val redirectLocation = therapist.googleCalendarIntegration.authorizeInGoogle()

            "должна возвращать корректный редирект" {
                redirectLocation.shouldBeRedirectToGoogleOAuth(clientId)
            }
        }

        "при корректном запросе с колбэком OAuth-авторизации" - {
            // Сетап
            val oAuthRequest = therapist.googleCalendarIntegration.authorizeInGoogle()
                .let { OAuthObjectMother.oAuth2AuthorizationRequest(it) }
            val aOAuthResponse = aOAuth2AuthorizationResponse(oAuthRequest.state)

            // Действие
            val response = therapist.googleCalendarIntegration.handleOAuthCallbackForResponse(aOAuthResponse)

            "должна возвращать очищенный редирект на URL обработчика авторизации в Google" {

                val location = response.redirectLocation()

                location.path shouldBe GoogleOAuthController.PATH
            }
        }

    }

    "метод обработки результата авторизации" - {
        "в случае успешной авторизации должен" - {
            // Сетап
            val googleEmail = faker.internet().emailAddress()
            val mockGoogleOAuthServer = MockGoogleOAuthServer(WireMock.wiremock)
            val mockGoogleCalendar = MockGoogleCalendar(WireMock.wiremock)
            val oAuthRequest = therapist.googleCalendarIntegration.authorizeInGoogle()
                .let { OAuthObjectMother.oAuth2AuthorizationRequest(it) }
            val aOAuthResponse = aOAuth2AuthorizationResponse(oAuthRequest.state)
            val accessToken = "accessToken"
            val refreshToken = "refreshToken"
            val calendars = emptyList<GoogleCalendar>()
            mockGoogleOAuthServer.OnGetToken(clientId, clientSecret, aOAuthResponse.code)
                .returnsToken(accessToken, refreshToken)
            mockGoogleOAuthServer.OnGetUserInfo(accessToken).returnsUserInfo(googleEmail)
            mockGoogleOAuthServer.OnRefreshToken(refreshToken).returnsToken(accessToken)
            mockGoogleCalendar.OnGetCalendars(accessToken).returnsCalendars(calendars)

            therapist.googleCalendarIntegration.handleOAuthCallbackForResponse(aOAuthResponse)

            // Действие
            val response = therapist.googleCalendarIntegration.finalizeOAuthCallbackForResponse()

            // Проверка
            "возвращать редирект на страницу календаря с параметром google-connected=true" {
                with(response.redirectLocation()) {
                    path shouldBe SchedulePageController.PATH
                    query shouldBe "google-connected=true"
                }
            }

            "обеспечивать возможность дальнейших запросов к Google Calendar" {
                val gotCalendars = googleCalendarsService.findGoogleAccountCalendars(THE_THERAPIST_REF)
                (gotCalendars.single().content as GoogleCalendarsSettingsView.Calendars).calendars shouldBe calendars
            }

        }

        "в случае успешной авторизации уже подключенного аккаунта должен" - {
            // Сетап
            val googleEmail = faker.internet().emailAddress()
            googleCalendarsTestApi.addAccount(aGoogleAccount(THE_THERAPIST_REF, googleEmail))

            val mockGoogleOAuthServer = MockGoogleOAuthServer(WireMock.wiremock)
            val mockGoogleCalendar = MockGoogleCalendar(WireMock.wiremock)
            val oAuthRequest = therapist.googleCalendarIntegration.authorizeInGoogle()
                .let { OAuthObjectMother.oAuth2AuthorizationRequest(it) }
            val aOAuthResponse = aOAuth2AuthorizationResponse(oAuthRequest.state)
            val accessToken = "accessToken"
            val refreshToken = "refreshToken"
            val calendars = emptyList<GoogleCalendar>()
            mockGoogleOAuthServer.OnGetToken(clientId, clientSecret, aOAuthResponse.code)
                .returnsToken(accessToken, refreshToken)
            mockGoogleOAuthServer.OnGetUserInfo(accessToken).returnsUserInfo(googleEmail)
            mockGoogleOAuthServer.OnRefreshToken(refreshToken).returnsToken(accessToken)
            mockGoogleCalendar.OnGetCalendars(accessToken).returnsCalendars(calendars)

            therapist.googleCalendarIntegration.handleOAuthCallbackForResponse(aOAuthResponse)

            // Действие
            val response = therapist.googleCalendarIntegration.finalizeOAuthCallbackForResponse()

            // Проверка
            "возвращать редирект на страницу календаря с параметром google-connected=true" {
                with(response.redirectLocation()) {
                    path shouldBe SchedulePageController.PATH
                    query shouldBe "google-connected=true"
                }
            }

            "обеспечивать возможность дальнейших запросов к Google Calendar" {
                val gotCalendars = googleCalendarsService.findGoogleAccountCalendars(THE_THERAPIST_REF)
                (gotCalendars.single().content as GoogleCalendarsSettingsView.Calendars).calendars shouldBe calendars
            }

        }
    }

})
