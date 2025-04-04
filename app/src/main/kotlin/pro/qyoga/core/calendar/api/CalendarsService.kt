package pro.qyoga.core.calendar.api

import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDateTime
import java.time.ZonedDateTime


interface CalendarsService {

    fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): Iterable<CalendarItem<*, LocalDateTime>>

}