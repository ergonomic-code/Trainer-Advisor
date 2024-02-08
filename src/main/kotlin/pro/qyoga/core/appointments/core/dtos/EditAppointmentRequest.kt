package pro.qyoga.core.appointments.core.dtos

import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.api.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class EditAppointmentRequest(

    val client: ClientRef,
    val appointmentType: AppointmentTypeRef?,
    val appointmentTypeTitle: String,
    val therapeuticTask: TherapeuticTaskRef?,
    val dateTime: LocalDateTime,
    val timeZone: ZoneId,
    val place: String?,
    val cost: Int?,
    val payed: Boolean?,
    val comment: String?

) {

    val instant: Instant = dateTime.atZone(timeZone).toInstant()

}