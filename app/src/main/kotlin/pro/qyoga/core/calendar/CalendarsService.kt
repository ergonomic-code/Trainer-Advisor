package pro.qyoga.core.calendar

import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDate
import java.time.ZoneId


interface CalendarsService {

    fun findAllByInterval(
        therapist: TherapistRef,
        from: LocalDate,
        to: LocalDate,
        currentUserTimeZone: ZoneId
    ): Iterable<LocalCalendarItem<*>>

}