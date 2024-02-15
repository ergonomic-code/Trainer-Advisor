package pro.qyoga.core.appointments.core

import org.springframework.data.annotation.*
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.api.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

typealias AppointmentRef = AggregateReference<Appointment, Long>

fun AppointmentRef(id: Long): AppointmentRef = AggregateReference.to(id)

@Table("appointments")
data class Appointment(

    val therapistRef: TherapistRef,
    val clientRef: ClientRef,
    val typeRef: AppointmentTypeRef,
    val therapeuticTaskRef: TherapeuticTaskRef?,
    val dateTime: Instant,
    val timeZone: ZoneId,
    val duration: Duration,
    val place: String?,
    val cost: Int?,
    val payed: Boolean,
    val status: AppointmentStatus,
    val comment: String?,

    @Id
    val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) {


    @Transient
    val wallClockDateTime: LocalDateTime = dateTime.atZone(timeZone).toLocalDateTime()

    @Transient
    val endWallClockDateTime: LocalDateTime = dateTime.plus(duration).atZone(timeZone).toLocalDateTime()

    constructor(
        therapist: TherapistRef,
        editAppointmentRequest: EditAppointmentRequest,
        typeRef: AppointmentTypeRef
    ) : this(
        therapist,
        editAppointmentRequest.client,
        typeRef,
        editAppointmentRequest.therapeuticTask,
        editAppointmentRequest.instant,
        editAppointmentRequest.timeZone,
        editAppointmentRequest.duration,
        editAppointmentRequest.place,
        editAppointmentRequest.cost,
        editAppointmentRequest.payed ?: false,
        editAppointmentRequest.appointmentStatus,
        editAppointmentRequest.comment,
    )

    init {
        require(cost == null || cost > 0) { "Cost should be non-negative" }
    }

    object Fetch {
        val summaryRefs = listOf(Appointment::clientRef, Appointment::typeRef)
        val editableRefs = summaryRefs + Appointment::therapeuticTaskRef
    }

}
