package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.app.therapist.appointments.core.CreateAppointment
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.appointments.randomAppointmentDate
import pro.qyoga.tests.fixture.data.randomTimeZone
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_REF
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class AppointmentsBackgrounds(
    private val appointmentsRepo: AppointmentsRepo,
    private val createAppointment: CreateAppointment,
    private val clientsBackgrounds: ClientsBackgrounds,
    private val therapeuticTasksBackgrounds: TherapeuticTasksBackgrounds,
) {

    fun findAll(therapistId: Long = THE_THERAPIST_ID): Iterable<Appointment> {
        return appointmentsRepo.findAll()
    }

    fun create(
        dateTime: LocalDateTime = randomAppointmentDate(),
        timeZone: ZoneId = randomTimeZone(),
        therapist: TherapistRef = THE_THERAPIST_REF
    ): Appointment {
        val clientRef = clientsBackgrounds.createClients(1, therapist.id!!).single().ref()
        val therapeuticTaskRef = therapeuticTasksBackgrounds.createTherapeuticTask(therapist.id!!).ref()
        return createAppointment(
            therapist,
            AppointmentsObjectMother.randomEditAppointmentRequest(
                client = clientRef,
                therapeuticTask = therapeuticTaskRef,
                dateTime = dateTime,
                timeZone = timeZone
            )
        )
    }

    fun create(
        editAppointmentRequest: EditAppointmentRequest,
        therapist: TherapistRef = THE_THERAPIST_REF
    ): Appointment {
        return createAppointment(therapist, editAppointmentRequest)
    }

}
