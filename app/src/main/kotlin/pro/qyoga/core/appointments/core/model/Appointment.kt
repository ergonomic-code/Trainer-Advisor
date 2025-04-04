package pro.qyoga.core.appointments.core.model

import org.springframework.data.annotation.*
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.ergo.hydration.Identifiable
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

typealias AppointmentRef = AggregateReference<Appointment, UUID>

fun AppointmentRef(id: UUID): AppointmentRef = AggregateReference.to(id)

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
    val externalId: String?,

    @Id
    override val id: UUID = UUIDv7.randomUUID(),
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) : Identifiable<UUID> {

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
        editAppointmentRequest.externalId
    )

    init {
        require(cost == null || cost > 0) { "Cost should be non-negative" }
        require(duration.toHours() <= 24) { "Appointment duration must be less or equal than 24 hours" }
    }

    object Fetch {
        val summaryRefs = listOf(Appointment::clientRef, Appointment::typeRef)
        val editableRefs = summaryRefs + Appointment::therapeuticTaskRef
    }

}
