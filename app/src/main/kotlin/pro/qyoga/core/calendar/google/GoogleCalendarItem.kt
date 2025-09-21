package pro.qyoga.core.calendar.google

import pro.azhidkov.platform.java.time.toLocalDateTime
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarItemId
import java.time.Duration
import java.time.ZoneId
import java.time.temporal.Temporal

data class GoogleCalendarItemId(
    val calendarId: String,
    val itemId: String
) : CalendarItemId {

    override val type: String = GoogleCalendar.TYPE

    override fun toQueryParamStr(): String =
        "$calendarId,$itemId"

}

data class GoogleCalendarItem<DATE : Temporal>(
    override val id: GoogleCalendarItemId,
    override val title: String,
    override val description: String,
    override val dateTime: DATE,
    override val duration: Duration,
    override val location: String?
) : CalendarItem<GoogleCalendarItemId, DATE> {

    fun toLocalizedCalendarItem(zoneId: ZoneId) =
        GoogleCalendarItem(
            id,
            title,
            description,
            dateTime.toLocalDateTime(zoneId),
            duration,
            location
        )

}
