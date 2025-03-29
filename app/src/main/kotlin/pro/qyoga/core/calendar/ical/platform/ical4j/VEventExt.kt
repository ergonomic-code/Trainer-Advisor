package pro.qyoga.core.calendar.ical.platform.ical4j

import net.fortuna.ical4j.model.Period
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.RecurrenceId
import pro.azhidkov.platform.java.time.toLocalDateTime
import pro.qyoga.core.calendar.ical.model.ICalEventId
import pro.qyoga.core.calendar.ical.model.LocalizedICalCalendarItem
import java.time.Duration
import java.time.ZoneId
import java.time.temporal.Temporal
import kotlin.jvm.optionals.getOrNull


val VEvent.id: ICalEventId
    get() = ICalEventId(this.uid.get().value, this.recurrenceId)

fun VEvent.toLocalizedICalCalendarItem(
    period: Period<Temporal>,
    zoneId: ZoneId
): LocalizedICalCalendarItem = LocalizedICalCalendarItem(
    ICalEventId(this.uid.orElseThrow().value, recurrenceId),
    this.summary.value,
    this.description?.value ?: "",
    period.start.toLocalDateTime(zoneId),
    period.duration as Duration
)

val VEvent.recurrenceId: String?
    get() = getProperty<RecurrenceId<String>>("RECURRENCE-ID").getOrNull()?.value