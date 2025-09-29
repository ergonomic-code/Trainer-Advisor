package pro.qyoga.core.calendar.api

import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.UriTemplate
import java.net.URI
import java.time.Duration
import java.time.temporal.Temporal

data class CalendarItemUri(
    val type: String,
    val params: Map<String, String?>
) {

    companion object {

        private const val SCHEMA = "qyoga"
        private const val HOST = "calendars"
        private const val PATH = "{type}/events"
        const val TEMPLATE = "$SCHEMA://$HOST/$PATH"

        private val pathTemplate = UriTemplate("/$PATH")

        fun parseOrNull(uri: URI): CalendarItemUri? {
            if (uri.scheme != SCHEMA) {
                return null
            }
            if (uri.host != HOST) {
                return null
            }

            val comps = UriComponentsBuilder.fromUri(uri).build()
            if (!pathTemplate.matches(comps.path)) {
                return null
            }

            val type = comps.pathSegments.getOrNull(0)
                ?: return null

            val params: Map<String, String?> = comps.queryParams.toSingleValueMap()

            return CalendarItemUri(type, params)
        }

    }

}

interface CalendarItemId {

    val type: CalendarType

    fun toUri(): URI = UriComponentsBuilder.fromUriString(CalendarItemUri.TEMPLATE)
        .queryParams(MultiValueMap.fromSingleValue(toMap()))
        .buildAndExpand(type.name)
        .toUri()

    fun toMap(): Map<String, String?>

}

interface CalendarItem<ID, DATE : Temporal> {
    val id: ID
    val title: String
    val description: String
    val dateTime: DATE
    val duration: Duration

    @Suppress("UNCHECKED_CAST")
    val endDateTime: DATE
        get() = this.dateTime.plus(this.duration) as DATE
    val location: String?
}
