package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.app.therapist.appointments.core.edit.CreateAppointmentWorkflow
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.appointments.randomAppointmentCost
import pro.qyoga.tests.fixture.appointments.randomAppointmentDate
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.data.randomSentence
import pro.qyoga.tests.fixture.data.randomTimeZone
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_REF
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.random.Random

@Component
class AppointmentsBackgrounds(
    private val appointmentsRepo: AppointmentsRepo,
    private val createAppointment: CreateAppointmentWorkflow,
    private val clientsBackgrounds: ClientsBackgrounds,
    private val therapeuticTasksBackgrounds: TherapeuticTasksBackgrounds,
) {

    fun findAll(therapistId: Long = THE_THERAPIST_ID): Iterable<Appointment> {
        return appointmentsRepo.findAll()
    }

    fun createFull(
        dateTime: LocalDateTime = randomAppointmentDate(),
        timeZone: ZoneId = randomTimeZone(),
        place: String? = randomCyrillicWord(),
        cost: Int? = randomAppointmentCost(),
        payed: Boolean? = Random.nextBoolean(),
        comment: String? = randomSentence(),
        therapist: TherapistRef = THE_THERAPIST_REF,
        therapeuticTaskRef: TherapeuticTaskRef? = therapeuticTasksBackgrounds.createTherapeuticTask(therapist.id!!)
            .ref(),
    ): Appointment {
        return create(dateTime, timeZone, place, cost, payed, comment, therapist, therapeuticTaskRef)
    }

    fun create(
        dateTime: LocalDateTime = randomAppointmentDate(),
        timeZone: ZoneId = randomTimeZone(),
        place: String? = null,
        cost: Int? = null,
        payed: Boolean? = null,
        comment: String? = null,
        therapist: TherapistRef = THE_THERAPIST_REF,
        therapeuticTaskRef: TherapeuticTaskRef? = null
    ): Appointment {
        val clientRef = clientsBackgrounds.createClients(1, therapist.id!!).single().ref()
        val appointment = createAppointment(
            therapist,
            AppointmentsObjectMother.randomEditAppointmentRequest(
                client = clientRef,
                therapeuticTask = therapeuticTaskRef,
                dateTime = dateTime,
                timeZone = timeZone,
                place = place,
                cost = cost,
                payed = payed,
                comment = comment
            )
        )
        return appointmentsRepo.findById(appointment.id, Appointment.Fetch.editableRefs)!!
    }

    fun create(
        editAppointmentRequest: EditAppointmentRequest,
        therapist: TherapistRef = THE_THERAPIST_REF
    ): Appointment {
        return createAppointment(therapist, editAppointmentRequest)
    }

}
