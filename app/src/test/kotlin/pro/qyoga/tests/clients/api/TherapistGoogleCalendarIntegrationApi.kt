package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.app.therapist.appointments.core.schedule.settings.GoogleCalendarSettingsController
import pro.qyoga.i9ns.calendars.google.model.GoogleAccountRef
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString
import pro.qyoga.tests.platform.spring.rest_test_client.redirectLocation
import java.net.URI

class TherapistGoogleCalendarIntegrationApi(
    override val authCookie: Cookie,
    private val restTestClient: RestTestClient
) : AuthorizedApi {

    fun authorizeInGoogle(): URI {
        val response = restTestClient.get()
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
    ): RestTestClient.ResponseSpec {
        return restTestClient.get()
            .uri {
                it.path("/therapist/oauth2/google/callback")
                    .queryParam("state", authResponse.state)
                    .queryParam("code", authResponse.code)
                    .build()
            }
            .authorized()
            .exchange()
    }

    fun finalizeOAuthCallbackForResponse(): RestTestClient.ResponseSpec {
        return restTestClient.get()
            .uri("/therapist/oauth2/google/callback")
            .authorized()
            .exchange()
    }

    fun getGoogleCalendarComponent(): Document {
        return restTestClient.get()
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

        restTestClient.patch()
            .uri(GoogleCalendarSettingsController.updateCalendarSettingsPath(googleAccount, calendarId))
            .body(body)
            .authorized()
            .exchange()
            .expectStatus().isNoContent
    }

}
