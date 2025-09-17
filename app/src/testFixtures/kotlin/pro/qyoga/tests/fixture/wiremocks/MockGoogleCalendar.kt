package pro.qyoga.tests.fixture.wiremocks

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.util.UriUtils
import pro.qyoga.core.calendar.google.GoogleCalendar
import pro.qyoga.core.calendar.google.GoogleCalendarItem
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.text.Charsets.UTF_8

/**
 * Mock implementation of Google Calendar API for testing purposes.
 */
@Component
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
                getCalendarsRequest()
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

        fun returnsForbidden() {
            wiremockServer.stubFor(
                getCalendarsRequest()
                    .willReturn(
                        aResponse()
                            .withStatus(HttpStatus.FORBIDDEN.value())
                            .withHeader("Content-Type", "application/json")
                            .withBody(
                                """
                                    {
                                        "code": 403,
                                        "details": [
                                        {
                                            "@type": "type.googleapis.com/google.rpc.ErrorInfo",
                                            "reason": "ACCESS_TOKEN_SCOPE_INSUFFICIENT"
                                        }
                                        ],
                                        "errors": [
                                        {
                                            "domain": "global",
                                            "message": "Insufficient Permission",
                                            "reason": "insufficientPermissions"
                                        }
                                        ],
                                        "message": "Request had insufficient authentication scopes.",
                                        "status": "PERMISSION_DENIED"
                                    }
                          """
                            )
                    )
            )
        }

        private fun getCalendarsRequest(): MappingBuilder = get(
            urlEqualTo(
                "/google/calendar/v3/users/me/calendarList"
            )
        )

    }

    inner class OnGetEvents(
        private val accessToken: String,
        private val calendarId: String
    ) {

        fun returnsEvents(vararg events: GoogleCalendarItem<*>) {
            wiremockServer.stubFor(
                get(
                    urlPathEqualTo(
                        "/google/calendar/v3/calendars/${
                            UriUtils.encodePathSegment(
                                calendarId,
                                UTF_8
                            )
                        }/events"
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

private fun GoogleCalendarItem<*>.toJson(): String {
    val start = ((dateTime as? ZonedDateTime) ?: (dateTime as LocalDateTime).atZone(asiaNovosibirskTimeZone))
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    val end = ((endDateTime as? ZonedDateTime) ?: (endDateTime as LocalDateTime).atZone(asiaNovosibirskTimeZone))
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
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