package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.app.therapist.appointments.core.edit.ops.CreateAppointmentOp
import pro.qyoga.app.therapist.appointments.core.schedule.GetCalendarAppointmentsOp
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.calendar.api.LocalCalendarItem
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.core.users.therapists.ref
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.data.randomSentence
import pro.qyoga.tests.fixture.data.randomTimeZone
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentCost
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentDate
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentDuration
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class AppointmentsBackgrounds(
    private val appointmentsRepo: AppointmentsRepo,
    private val createAppointment: CreateAppointmentOp,
    private val getCalendarAppointments: GetCalendarAppointmentsOp,
    private val clientsBackgrounds: ClientsBackgrounds,
    private val therapeuticTasksBackgrounds: TherapeuticTasksBackgrounds,
) {

    fun findAll(therapistId: UUID = THE_THERAPIST_ID): Iterable<Appointment> {
        return appointmentsRepo.findAll()
            .filter { it.therapistRef.id == therapistId }
    }

    fun getDaySchedule(
        date: LocalDate,
        therapistUserDetails: QyogaUserDetails = theTherapistUserDetails
    ): Iterable<LocalCalendarItem<*>> {
        return getCalendarAppointments(therapistUserDetails.ref, date)
    }

    fun createFull(
        dateTime: LocalDateTime = randomAppointmentDate(),
        timeZone: ZoneId = randomTimeZone(),
        duration: Duration = randomAppointmentDuration(),
        place: String? = randomCyrillicWord(),
        cost: Int? = randomAppointmentCost(),
        payed: Boolean? = faker.random().nextBoolean(),
        comment: String? = randomSentence(),
        therapist: TherapistRef = THE_THERAPIST_REF,
        therapeuticTaskRef: TherapeuticTaskRef? = therapeuticTasksBackgrounds.createTherapeuticTask(therapist.id!!)
            .ref(),
    ): Appointment {
        return create(dateTime, timeZone, duration, place, cost, payed, comment, therapist, therapeuticTaskRef)
    }

    fun create(
        dateTime: LocalDateTime = randomAppointmentDate(),
        timeZone: ZoneId = randomTimeZone(),
        duration: Duration = randomAppointmentDuration(),
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
                duration = duration,
                place = place,
                cost = cost,
                payed = payed,
                comment = comment
            )
        )
        return appointmentsRepo.findById(appointment.id, Appointment.Fetch.editableRefs)!!
    }

    fun findById(appointmentId: UUID): Appointment? {
        return appointmentsRepo.findByIdOrNull(appointmentId)
    }

}
