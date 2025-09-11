package pro.qyoga.core.calendar.google

import pro.qyoga.core.calendar.api.CalendarItem
import java.time.Duration
import java.time.LocalDateTime

@JvmInline
value class GoogleCalendarItemId(val value: String)

data class GoogleCalendarItem(
    override val id: GoogleCalendarItemId,
    override val title: String,
    override val description: String,
    override val dateTime: LocalDateTime,
    override val duration: Duration,
    override val location: String?
) : CalendarItem<GoogleCalendarItemId, LocalDateTime>
