package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.FutureAppointments
import pro.qyoga.core.appointments.core.findAllFutureAppointments
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant


@Component
class GetFutureAppointmentsWorkflow(
    private val userSettingsRepo: UserSettingsRepo,
    private val appointmentsRepo: AppointmentsRepo
) : (TherapistRef) -> FutureAppointments {

    override fun invoke(therapist: TherapistRef): FutureAppointments {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapist))

        val now = Instant.now()
        val futureAppointmentsList =
            appointmentsRepo.findAllFutureAppointments(therapist, now, currentUserTimeZone)

        return FutureAppointments.of(now.atZone(currentUserTimeZone), futureAppointmentsList.toList())
    }

}
