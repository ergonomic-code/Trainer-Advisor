package pro.qyoga.core.calendar.ical.model

import net.fortuna.ical4j.model.Period
import net.fortuna.ical4j.model.component.VEvent
import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.calendar.api.Calendar
import pro.qyoga.core.calendar.ical.platform.ical4j.parseIcs
import pro.qyoga.core.calendar.ical.platform.ical4j.recurrenceId
import pro.qyoga.core.users.therapists.TherapistRef
import java.net.URL
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.Temporal
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

    companion object {
        const val TYPE = "ICAL"
    }

}

fun ICalCalendar.vEvents(): List<VEvent> =
    calendar.getComponents<VEvent>("VEVENT")

fun ICalCalendar.findById(eventId: ICalEventId) =
    vEvents().find { it.uid.get().value == eventId.uid && it.recurrenceId == eventId.recurrenceId }

fun ICalCalendar.vEventOccurrencesIn(
    period: Period<ZonedDateTime>
): List<Pair<VEvent, Period<Temporal>>> = vEvents().flatMap { c: VEvent ->
    c.calculateRecurrenceSet<Temporal>(period).map { c to it }
}