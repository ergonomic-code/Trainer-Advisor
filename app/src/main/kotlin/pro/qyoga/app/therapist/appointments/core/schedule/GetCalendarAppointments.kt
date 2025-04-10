package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.google.GoogleCalendarsService
import pro.qyoga.core.calendar.ical.ICalCalendarsRepo
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.*


@Component
class GetCalendarAppointmentsOp(
    private val userSettingsRepo: UserSettingsRepo,
    private val appointmentsRepo: AppointmentsRepo,
    private val iCalCalendarsRepo: ICalCalendarsRepo,
    private val googleCalendarsService: GoogleCalendarsService
) : (TherapistRef, LocalDate) -> Iterable<CalendarItem<*, LocalDateTime>> {

    override fun invoke(therapist: TherapistRef, date: LocalDate): Iterable<CalendarItem<*, LocalDateTime>> {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapist))
        val interval = calendarIntervalAround(date, currentUserTimeZone)
        val appointments = appointmentsRepo.findCalendarItemsInInterval(therapist, interval)
        val drafts = iCalCalendarsRepo.findCalendarItemsInInterval(therapist, interval)

        try {
            googleCalendarsService.findCalendarItemsInInterval(therapist, interval)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return appointments + drafts
    }

}

private fun calendarIntervalAround(
    date: LocalDate,
    currentUserTimeZone: ZoneId
): Interval<ZonedDateTime> {
    val from = date.minusDays((CalendarPageModel.DAYS_IN_CALENDAR / 2).toLong()).atStartOfDay(currentUserTimeZone)
    return Interval.of(from, Duration.ofDays(CalendarPageModel.DAYS_IN_CALENDAR.toLong()))
}
