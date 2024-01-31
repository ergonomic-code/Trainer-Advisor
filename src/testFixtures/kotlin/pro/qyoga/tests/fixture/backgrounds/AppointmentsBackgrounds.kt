package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID

@Component
class AppointmentsBackgrounds(
    private val appointmentsRepo: AppointmentsRepo
) {

    fun findAll(therapistId: Long = THE_THERAPIST_ID): Iterable<Appointment> {
        return appointmentsRepo.findAll()
    }

}
