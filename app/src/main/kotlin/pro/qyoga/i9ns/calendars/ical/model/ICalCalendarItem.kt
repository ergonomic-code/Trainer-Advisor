package pro.qyoga.i9ns.calendars.ical.model

import pro.qyoga.core.calendar.api.CalendarItem
import java.time.Duration
import java.time.ZonedDateTime


data class ICalCalendarItem(
    override val id: ICalEventId,
    override val title: String,
    override val description: String,
    override val dateTime: ZonedDateTime,
    override val duration: Duration,
    override val location: String?
) : CalendarItem<ICalEventId, ZonedDateTime>

fun ICalCalendarItem.toLocalizedICalCalendarItem(): LocalizedICalCalendarItem =
    LocalizedICalCalendarItem(
        this.id,
        this.title,
        this.description,
        this.dateTime.toLocalDateTime(),
        this.duration,
        this.location
    )
