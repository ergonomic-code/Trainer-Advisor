package pro.qyoga.core.appointments.core.model

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.api.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


@Table("appointments")
data class Appointment(

    val therapistRef: TherapistRef,
    val clientRef: ClientRef,
    val typeRef: AppointmentTypeRef,
    val therapeuticTaskRef: TherapeuticTaskRef?,
    val dateTime: Instant,
    val timeZone: ZoneId,
    val place: String?,
    val cost: Int?,
    val payed: Boolean,
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

    constructor(
        therapist: TherapistRef,
        editAppointmentRequest: EditAppointmentRequest,
        typeRef: AppointmentTypeRef
    ) : this(
        therapist,
        editAppointmentRequest.client,
        typeRef,
        editAppointmentRequest.therapeuticTask,
        editAppointmentRequest.dateTime.toInstant(editAppointmentRequest.timeZone.rules.getOffset(Instant.now())),
        editAppointmentRequest.timeZone,
        editAppointmentRequest.place,
        editAppointmentRequest.cost,
        editAppointmentRequest.payed ?: false,
        editAppointmentRequest.comment,
    )

    init {
        require(cost == null || cost > 0) { "Cost should be non-negative" }
    }

    @Transient
    val wallClockDateTime: ZonedDateTime = dateTime.atZone(timeZone)

}