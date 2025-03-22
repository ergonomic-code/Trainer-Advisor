package pro.qyoga.tests.fixture.object_mothers.appointments

import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrNull
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.appointments.core.*
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.tests.fixture.data.*
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object AppointmentsObjectMother {

    fun randomLocalizedAppointmentSummary(
        client: ClientRef = ClientsObjectMother.fakeClientRef,
        typeTitle: String = randomCyrillicWord(),
        dateTime: LocalDateTime = randomAppointmentDate(),
        duration: Duration = randomAppointmentDuration(),
        appointmentStatus: AppointmentStatus = AppointmentStatus.entries.randomElement(),
    ): LocalizedAppointmentSummary {
        return LocalizedAppointmentSummary(
            aAppointmentId().id!!,
            client.resolveOrThrow().fullName(),
            typeTitle,
            dateTime,
            duration,
            appointmentStatus
        )
    }

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
        appointmentStatus: AppointmentStatus = AppointmentStatus.entries.randomElement(),
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
        payed: Boolean = faker.random().nextBoolean(),
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
        appointmentStatus: AppointmentStatus = AppointmentStatus.entries.randomElement(),
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

    fun aAppointmentId(): AppointmentRef =
        AppointmentRef(UUIDv7.randomUUID())

}

fun randomAppointmentDate(): LocalDateTime =
    randomLocalDate(LocalDate.now(), Duration.ofDays(30))
        .atTime(randomWorkingTime())

fun randomAppointmentCost(): Int = faker.random().nextInt(0, 10_000)

fun randomAppointmentDuration(): Duration {
    val fraction = 15
    val fractions = 3L * 60 / 15
    return Duration.ofMinutes(fraction * faker.random().nextLong(1, fractions))
}
