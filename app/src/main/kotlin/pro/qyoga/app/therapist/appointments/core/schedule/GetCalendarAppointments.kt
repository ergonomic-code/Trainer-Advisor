package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.Interval
import pro.azhidkov.platform.kotlin.tryExecute
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.SearchResult
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.GoogleCalendarsService
import pro.qyoga.i9ns.calendars.ical.ICalCalendarsRepo
import java.time.*


data class GetCalendarAppointmentsRs(
    val appointments: List<CalendarItem<*, LocalDateTime>>,
    val hasErrors: Boolean
)

@Component
class GetCalendarAppointmentsOp(
    private val userSettingsRepo: UserSettingsRepo,
    private val appointmentsRepo: AppointmentsRepo,
    private val iCalCalendarsRepo: ICalCalendarsRepo,
    private val googleCalendarsService: GoogleCalendarsService
) : (TherapistRef, LocalDate) -> GetCalendarAppointmentsRs {

    override fun invoke(therapist: TherapistRef, date: LocalDate): GetCalendarAppointmentsRs {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapist))
        val interval = calendarIntervalAround(date, currentUserTimeZone)
        val appointments = appointmentsRepo.findCalendarItemsInInterval(therapist, interval)

        val iCalEventsResult =
            tryExecute { iCalCalendarsRepo.findCalendarItemsInInterval(therapist, interval) }

        val googleCalendarEventsResult =
            tryExecute { googleCalendarsService.findCalendarItemsInInterval(therapist, interval) }

        val drafts = iCalEventsResult.items() + googleCalendarEventsResult.items()

        val hasErrors = iCalEventsResult.hasErrors() || googleCalendarEventsResult.hasErrors()

        return GetCalendarAppointmentsRs(appointments + drafts, hasErrors)
    }

}

private fun calendarIntervalAround(
    date: LocalDate,
    currentUserTimeZone: ZoneId
): Interval<ZonedDateTime> {
    val from = date.minusDays((CalendarPageModel.DAYS_IN_CALENDAR / 2).toLong()).atStartOfDay(currentUserTimeZone)
    return Interval.of(from, Duration.ofDays(CalendarPageModel.DAYS_IN_CALENDAR.toLong()))
}

private fun Result<SearchResult<*>>.items(): Iterable<CalendarItem<out Any?, LocalDateTime>> =
    this.getOrNull() ?: emptyList()

private fun Result<SearchResult<*>>.hasErrors() =
    this.isFailure || this.getOrThrow().hasErrors
