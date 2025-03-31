package pro.qyoga.core.calendar.ical.platform.ical4j

import net.fortuna.ical4j.model.Period
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.parameter.TzId
import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.model.property.RecurrenceId
import pro.azhidkov.platform.java.time.toLocalDateTime
import pro.qyoga.core.calendar.ical.model.ICalCalendarItem
import pro.qyoga.core.calendar.ical.model.ICalEventId
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.Temporal
import java.time.temporal.TemporalQueries.zone
import kotlin.jvm.optionals.getOrNull


val VEvent.id: ICalEventId
    get() = ICalEventId(this.uid.get().value, this.recurrenceId)

val VEvent.title: String
    get() = this.summary.value

val VEvent.descriptionOrNull: String?
    get() = this.description?.value

val VEvent.recurrenceId: String?
    get() = getProperty<RecurrenceId<String>>("RECURRENCE-ID").getOrNull()?.value

val VEvent.startDateTime: ZonedDateTime
    get() = this.getDateTimeStart<Temporal>()
        .let { it: DtStart<Temporal> ->
            val date = it.date
            when {
                date is ZonedDateTime -> date.withZoneSameInstant(it.zoneId)
                date.query(zone()) != null -> ZonedDateTime.from(date)
                else -> ZonedDateTime.of(date.toLocalDateTime(ZoneId.systemDefault()), ZoneId.systemDefault())
            }
        }

val DtStart<Temporal>.zoneId: ZoneId
    get() = ZoneId.of(getParameter<TzId>("TZID").get().value)

val VEvent.javaDuration: Duration
    get() = if (this.duration != null) {
        Duration.parse(this.duration.value)
    } else {
        Duration.between(this.getDateTimeStart<Temporal>().date, this.getDateTimeEnd<Temporal>().date)
    }

val VEvent.geographicsPosOrNull: String?
    get() = this.geographicPos?.value

val VEvent.locationOrNull: String?
    get() = this.location?.value

fun VEvent.toICalCalendarItem(): ICalCalendarItem =
    toICalCalendarItem(Period(startDateTime, startDateTime + javaDuration))

fun VEvent.toICalCalendarItem(period: Period<ZonedDateTime>): ICalCalendarItem = ICalCalendarItem(
    id,
    title,
    descriptionOrNull ?: "",
    period.start,
    period.duration as Duration,
    listOfNotNull(geographicsPosOrNull, locationOrNull).joinToString(", ")
)