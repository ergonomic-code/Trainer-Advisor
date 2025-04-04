package pro.qyoga.core.calendar.ical.model

import net.fortuna.ical4j.model.component.VEvent
import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.java.time.Interval
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.calendar.api.Calendar
import pro.qyoga.core.calendar.ical.platform.ical4j.parseIcs
import pro.qyoga.core.calendar.ical.platform.ical4j.recurrenceId
import pro.qyoga.core.calendar.ical.platform.ical4j.toICalCalendarItem
import pro.qyoga.core.calendar.ical.platform.ical4j.toICalPeriod
import pro.qyoga.core.users.therapists.TherapistRef
import java.net.URL
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*


@Table("ical_calendars")
data class ICalCalendar(
    override val ownerRef: TherapistRef,
    override val name: String,
    val icsUrl: URL,
    val icsFile: String,

    @Id
    val id: UUID = UUIDv7.randomUUID(),
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val lastModifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) : Calendar {

    @Transient
    override val type: String = TYPE

    val calendar by lazy {
        parseIcs(icsFile)
    }

    fun withIcsFile(icsFile: String) =
        copy(icsFile = icsFile)

    companion object {
        const val TYPE = "ICAL"
    }

}

fun ICalCalendar.vEvents(): List<VEvent> =
    calendar.getComponents("VEVENT")

fun ICalCalendar.findById(eventId: ICalEventId) =
    vEvents()
        .find { it.uid.get().value == eventId.uid && it.recurrenceId == eventId.recurrenceId }

fun ICalCalendar.calendarItemsIn(
    interval: Interval<ZonedDateTime>
): List<ICalCalendarItem> =
    vEvents()
        .flatMap { ve: VEvent ->
            ve.calculateRecurrenceSet<ZonedDateTime>(interval.toICalPeriod())
                .map { ve.toICalCalendarItem(it) }
        }