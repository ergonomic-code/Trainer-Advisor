package pro.qyoga.i9ns.calendars.ical.model

import pro.qyoga.core.calendar.api.CalendarItem
import java.time.Duration
import java.time.LocalDateTime


data class LocalizedICalCalendarItem(
    override val id: ICalEventId,
    override val title: String,
    override val description: String,
    override val dateTime: LocalDateTime,
    override val duration: Duration,
    override val location: String?
) : CalendarItem<ICalEventId, LocalDateTime>
