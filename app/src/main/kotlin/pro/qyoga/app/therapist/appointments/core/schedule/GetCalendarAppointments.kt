package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.findAllByInterval
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDate
import java.time.ZoneId


@Component
class GetCalendarAppointmentsWorkflow(
    private val userSettingsRepo: UserSettingsRepo,
    private val appointmentsRepo: AppointmentsRepo
) : (TherapistRef, LocalDate) -> Pair<ZoneId, Collection<Appointment>> {

    override fun invoke(therapist: TherapistRef, date: LocalDate): Pair<ZoneId, Collection<Appointment>> {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapist))
        val appointments = appointmentsRepo.findAllByInterval(therapist, date.minusDays(1), date.plusDays(1), currentUserTimeZone)
        return Pair(currentUserTimeZone, appointments)
    }

}
