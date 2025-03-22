package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.calendar.LocalCalendarItem
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDate


@Component
class GetCalendarAppointmentsOp(
    private val userSettingsRepo: UserSettingsRepo,
    private val appointmentsRepo: AppointmentsRepo
) : (TherapistRef, LocalDate) -> Iterable<LocalCalendarItem<*>> {

    override fun invoke(therapist: TherapistRef, date: LocalDate): Iterable<LocalCalendarItem<*>> {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapist))
        val appointments =
            appointmentsRepo.findAllByInterval(therapist, date.minusDays(1), date.plusDays(1), currentUserTimeZone)
        return appointments
    }

}
