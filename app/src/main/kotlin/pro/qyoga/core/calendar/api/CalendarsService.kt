package pro.qyoga.core.calendar.api

import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDateTime
import java.time.ZonedDateTime


data class SearchResult<ID>(
    val items: Iterable<CalendarItem<ID, LocalDateTime>>,
    val hasErrors: Boolean = false
) : Iterable<CalendarItem<ID, LocalDateTime>> by items

interface CalendarsService<ID> {

    val type: CalendarType

    fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): SearchResult<ID>

    fun findById(therapistRef: TherapistRef, eventId: ID): CalendarItem<ID, ZonedDateTime>?

    fun parseStringId(sourceItem: SourceItem): ID

}
