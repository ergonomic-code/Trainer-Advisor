package pro.qyoga.tests.fixture.wiremocks

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.oauth2.GoogleOAuthController


typealias GoogleAccessToken = String

@Component
class MockGoogleOAuthServer(
    private val wiremockServer: WireMockServer
) {

    inner class OnGetToken(
        private val clientId: String,
        private val clientSecret: String,
        private val code: String,
    ) {

        fun returnsToken(
            authToken: String = "authToken",
            refreshToken: String = "refreshToken"
        ) {

            wiremockServer
                .stubFor(
                    post(urlEqualTo("/google/oauth/token"))
                        .withFormParam("grant_type", equalTo("authorization_code"))
                        .withFormParam("code", equalTo(code.replace("+", " ")))
                        .withFormParam("redirect_uri", matching(".*${GoogleOAuthController.PATH}"))
                        .withFormParam("client_id", equalTo(clientId))
                        .withFormParam("client_secret", equalTo(clientSecret))
                        .willReturn(
                            aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                    """
                                          {
                                              "access_token": "$authToken",
                                              "token_type": "Bearer",
                                              "expires_in": 3599,
                                              "refresh_token": "$refreshToken",
                                              "scope": "https://www.googleapis.com/auth/calendar"
                                          }
                                      """
                                )
                        )
                )
        }

    }

    inner class OnGetUserInfo(private val accessToken: GoogleAccessToken) {

        fun returnsUserInfo(googleEmail: String) {
            wiremockServer.stubFor(
                get(urlEqualTo("/google/oauth/userinfo"))
                    .withHeader("Authorization", equalTo("Bearer $accessToken"))
                    .willReturn(
                        aResponse()
                            .withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type", "application/json")
                            .withBody(
                                """
                                    {
                                        "email": "$googleEmail"
                                    }
                                """
                            )
                    )
            )
        }

    }

    inner class OnRefreshToken(private val refreshToken: String) {

        fun returnsToken(
            accessToken: GoogleAccessToken = "authToken"
        ) {
            wiremockServer
                .stubFor(
                    post(urlEqualTo("/google/oauth/token"))
                        .withFormParam("grant_type", equalTo("refresh_token"))
                        .withFormParam("refresh_token", equalTo(refreshToken))
                        .willReturn(
                            aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody(
                                    """
                                          {
                                              "access_token": "$accessToken",
                                              "token_type": "Bearer",
                                              "expires_in": 3599,
                                              "refresh_token": "$refreshToken",
                                              "scope": "https://www.googleapis.com/auth/calendar"
                                          }
                                      """
                                )
                        )
                )
        }
    }

}
