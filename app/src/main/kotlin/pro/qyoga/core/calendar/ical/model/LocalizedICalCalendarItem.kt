package pro.qyoga.core.calendar.ical.model

import pro.qyoga.core.calendar.api.LocalCalendarItem
import java.time.Duration
import java.time.LocalDateTime


data class LocalizedICalCalendarItem(
    override val id: ICalEventId,
    override val title: String,
    override val description: String,
    override val dateTime: LocalDateTime,
    override val duration: Duration,
) : LocalCalendarItem<ICalEventId>