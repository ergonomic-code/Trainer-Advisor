package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.findPastAppointmentsSlice
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant


@Component
class GetPastAppointmentsWorkflow(
    private val userSettingsRepo: UserSettingsRepo,
    private val appointmentsRepo: AppointmentsRepo
) : (TherapistRef) -> Collection<Appointment> {

    override fun invoke(therapist: TherapistRef): Collection<Appointment> {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapist))
        val now = Instant.now()
        return appointmentsRepo.findPastAppointmentsSlice(therapist, now, currentUserTimeZone)
    }

}
