package pro.qyoga.core.appointments.core

import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.api.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class EditAppointmentRequest(

    val client: ClientRef,
    val clientTitle: String,
    val appointmentType: AppointmentTypeRef?,
    val appointmentTypeTitle: String,
    val therapeuticTask: TherapeuticTaskRef?,
    val therapeuticTaskTitle: String?,
    val dateTime: LocalDateTime,
    val timeZone: ZoneId,
    val timeZoneTitle: String,
    val duration: Duration,
    val place: String?,
    val cost: Int?,
    val payed: Boolean?,
    val appointmentStatus: AppointmentStatus,
    val comment: String?

) {

    val instant: Instant = dateTime.atZone(timeZone).toInstant()

}