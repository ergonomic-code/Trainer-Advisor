package pro.qyoga.tests.fixture.wiremocks

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.springframework.http.HttpStatus
import pro.qyoga.core.calendar.google.GoogleCalendar
import pro.qyoga.core.calendar.google.GoogleCalendarItem
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import java.time.format.DateTimeFormatter

/**
 * Mock implementation of Google Calendar API for testing purposes.
 */
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
                get(
                    urlEqualTo(
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

    inner class OnGetEvents(
        private val accessToken: String,
        private val calendarId: String
    ) {

        fun returnsEvents(vararg events: GoogleCalendarItem) {
            wiremockServer.stubFor(
                get(
                    urlPathEqualTo("/google/calendar/v3/calendars/$calendarId/events")
                )
                    .withHeader("Authorization", equalTo("Bearer $accessToken"))
                    .willReturn(
                        aResponse()
                            .withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type", "application/json")
                            .withBody(
                                """
                              {
                                  "items": [${events.joinToString(",") { it.toJson() }}]
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
    """

private fun GoogleCalendarItem.toJson(): String {
    val start = dateTime.atZone(asiaNovosibirskTimeZone).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    val end = endDateTime.atZone(asiaNovosibirskTimeZone).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    return """
        {
            "id": "$id",
            "summary": "$title",
            "description": "$description",
            "start": {
                "dateTime": "$start",
                "timeZone": "${asiaNovosibirskTimeZone.id}"
            },
            "end": {
                "dateTime": "$end",
                "timeZone": "${asiaNovosibirskTimeZone.id}"
            }
        }
    """.trimIndent()
}