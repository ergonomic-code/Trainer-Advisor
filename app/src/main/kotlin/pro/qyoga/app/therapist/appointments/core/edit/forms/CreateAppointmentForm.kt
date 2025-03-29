package pro.qyoga.app.therapist.appointments.core.edit.forms

import pro.qyoga.core.appointments.core.model.AppointmentStatus
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId


data class CreateAppointmentForm(
    val client: ClientRef?,
    val clientTitle: String?,
    val appointmentType: AppointmentTypeRef?,
    val appointmentTypeTitle: String?,
    val therapeuticTask: TherapeuticTaskRef?,
    val therapeuticTaskTitle: String?,
    val dateTime: LocalDateTime?,
    val timeZone: ZoneId?,
    val timeZoneTitle: String?,
    val duration: Duration?,
    val place: String?,
    val cost: Int?,
    val payed: Boolean?,
    val appointmentStatus: AppointmentStatus?,
    val comment: String?,
    val externalId: String?
)
