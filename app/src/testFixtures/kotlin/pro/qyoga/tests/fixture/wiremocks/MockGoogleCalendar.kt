package pro.qyoga.tests.fixture.wiremocks

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import org.springframework.http.HttpStatus
import pro.qyoga.core.calendar.google.GoogleCalendar


class MockGoogleCalendar(
    private val wiremockServer: WireMockServer
) {

    inner class OnGetCalendars(
        private val accessToken: String
    ) {
        fun returnsCalendars(
            calendars: List<GoogleCalendar>
        ) {
            wiremockServer.stubFor(
                WireMock.get(
                    WireMock.urlEqualTo(
                        "/google/calendar/v3/users/me/calendarList"
                    )
                )
                    .withHeader("Authorization", equalTo("Bearer $accessToken"))
                    .willReturn(
                        aResponse()
                            .withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type", "application/json")
                            .withBody(
                                """
                              {
                                 "items": [${calendars.joinToString(",") { it.toJson() }}]
                              }
                          """
                            )
                    )
            )
        }
    }

}

private fun GoogleCalendar.toJson(): String =
    """
        {
            "id": "${this.externalId}",
            "summary": "${this.name}"
        }
    """.trimIndent()
