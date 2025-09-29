package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters.fromValue
import pro.qyoga.app.therapist.appointments.core.schedule.GoogleCalendarSettingsController
import pro.qyoga.i9ns.calendars.google.model.GoogleAccountRef
import pro.qyoga.tests.platform.spring.web_test_client.getBodyAsString
import pro.qyoga.tests.platform.spring.web_test_client.redirectLocation
import java.net.URI

class TherapistGoogleCalendarIntegrationApi(
    override val authCookie: Cookie,
    private val webTestClient: WebTestClient
) : AuthorizedApi {

    fun authorizeInGoogle(): URI {
        val response = webTestClient.get()
            .uri("/oauth2/authorization/google")
            .authorized()
            .exchange()
            .expectStatus().isFound

        return response.redirectLocation()
    }

    // curl 'http://localhost:8080/therapist/oauth2/google/callback?
    //           state=EWWVlMDBuTT8NzQvO1MZjrpRa3Kr6Jz_8WRkK9d3gBA%3D^&
    //           code=4/0AVMBsJi2rnHTaWamYvvRVOxmp8CkQQ4ymoRZsiASDMjPnX7phooZ-P5YnOyQuunowx8F2g^&
    //           scope=https://www.googleapis.com/auth/calendar.readonly' \
    fun handleOAuthCallbackForResponse(
        authResponse: OAuth2AuthorizationResponse
    ): WebTestClient.ResponseSpec {
        return webTestClient.get()
            .uri {
                it.path("/therapist/oauth2/google/callback")
                    .queryParam("state", authResponse.state)
                    .queryParam("code", authResponse.code)
                    .build()
            }
            .authorized()
            .exchange()
    }

    fun finalizeOAuthCallbackForResponse(): WebTestClient.ResponseSpec {
        return webTestClient.get()
            .uri("/therapist/oauth2/google/callback")
            .authorized()
            .exchange()
    }

    fun getGoogleCalendarComponent(): Document {
        return webTestClient.get()
            .uri(GoogleCalendarSettingsController.PATH)
            .authorized()
            .exchange()
            .expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun setShouldBeShown(googleAccount: GoogleAccountRef, calendarId: String, shouldBeShown: Boolean) {
        val body = mapOf(
            "shouldBeShown" to shouldBeShown
        )

        webTestClient.patch()
            .uri(GoogleCalendarSettingsController.updateCalendarSettingsPath(googleAccount, calendarId))
            .body(fromValue(body))
            .authorized()
            .exchange()
            .expectStatus().isNoContent
    }

}
