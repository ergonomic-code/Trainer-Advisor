package pro.qyoga.i9ns.calendars.ical.model

import pro.qyoga.core.calendar.api.CalendarItem
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.Temporal


data class ICalCalendarItem<DATE : Temporal>(
    override val id: ICalEventId,
    override val title: String,
    override val description: String,
    override val dateTime: DATE,
    override val duration: Duration,
    override val location: String?
) : CalendarItem<ICalEventId, DATE>

fun ICalCalendarItem<ZonedDateTime>.toLocalizedICalCalendarItem(): ICalCalendarItem<LocalDateTime> =
    ICalCalendarItem(
        this.id,
        this.title,
        this.description,
        this.dateTime.toLocalDateTime(),
        this.duration,
        this.location
    )
