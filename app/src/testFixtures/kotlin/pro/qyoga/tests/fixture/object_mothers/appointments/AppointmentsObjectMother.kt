package pro.qyoga.tests.fixture.object_mothers.appointments

import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrNull
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.core.appointments.core.AppointmentStatus
import pro.qyoga.core.appointments.core.EditAppointmentRequest
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.api.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.tests.fixture.data.*
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.random.Random

object AppointmentsObjectMother {

    fun randomEditAppointmentRequest(
        client: ClientRef = ClientsObjectMother.fakeClientRef,
        typeId: AppointmentTypeRef? = null,
        typeTitle: String = randomCyrillicWord(),
        therapeuticTask: TherapeuticTaskRef? = null,
        dateTime: LocalDateTime = randomAppointmentDate(),
        timeZone: ZoneId = randomTimeZone(),
        duration: Duration = randomAppointmentDuration(),
        place: String? = null,
        cost: Int? = null,
        payed: Boolean? = null,
        appointmentStatus: AppointmentStatus = AppointmentStatus.entries.random(),
        comment: String? = null
    ): EditAppointmentRequest {
        return EditAppointmentRequest(
            client,
            client.resolveOrNull()?.fullName() ?: randomCyrillicWord(),
            typeId,
            typeTitle,
            therapeuticTask,
            therapeuticTask.resolveOrNull()?.name ?: "",
            dateTime,
            timeZone,
            timeZone.id,
            duration,
            place,
            cost,
            payed,
            appointmentStatus,
            comment,
        )
    }

    fun randomFullEditAppointmentRequest(
        client: ClientRef,
        typeTitle: String = randomCyrillicWord(),
        therapeuticTask: TherapeuticTaskRef,
        place: String = randomSentence(),
        cost: Int = randomAppointmentCost(),
        payed: Boolean = Random.nextBoolean(),
        comment: String = randomSentence()
    ) = randomEditAppointmentRequest(
        client,
        typeTitle = typeTitle,
        therapeuticTask = therapeuticTask,
        place = place,
        cost = cost,
        payed = payed,
        comment = comment
    )

    fun appointmentPatchRequest(
        appointment: Appointment,
        appointmentType: AppointmentTypeRef?,
        appointmentTypeTitle: String?,
        therapeuticTask: TherapeuticTaskRef?,
        place: String?,
        cost: Int?,
        payed: Boolean?,
        appointmentStatus: AppointmentStatus = AppointmentStatus.entries.random(),
        comment: String?
    ): EditAppointmentRequest =
        EditAppointmentRequest(
            appointment.clientRef,
            appointment.clientRef.resolveOrNull()?.fullName() ?: "",
            appointmentType ?: appointment.typeRef,
            appointmentTypeTitle ?: appointment.typeRef.resolveOrThrow().name,
            therapeuticTask ?: appointment.therapeuticTaskRef,
            appointment.therapeuticTaskRef.resolveOrNull()?.name ?: "",
            appointment.wallClockDateTime,
            appointment.timeZone,
            appointment.timeZone.id,
            appointment.duration,
            place ?: appointment.place,
            cost ?: appointment.cost,
            payed ?: appointment.payed,
            appointmentStatus,
            comment ?: appointment.comment,
        )

}

fun randomAppointmentDate(): LocalDateTime =
    randomLocalDate(LocalDate.now(), Duration.ofDays(30))
        .atTime(randomWorkingTime())

fun randomAppointmentCost(): Int = Random.nextInt(0, 10_000)

fun randomAppointmentDuration(): Duration {
    val fraction = 15
    val fractions = 3L * 60 / 15
    return Duration.ofMinutes(fraction * Random.nextLong(1, fractions))
}
