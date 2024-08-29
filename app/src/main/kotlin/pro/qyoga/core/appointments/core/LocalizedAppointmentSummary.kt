package pro.qyoga.core.appointments.core

import java.time.Duration
import java.time.LocalDateTime


data class LocalizedAppointmentSummary(
    val id: Long,
    val clientName: String,
    val typeName: String,
    val therapeuticTaskName: String?,
    val dateTime: LocalDateTime,
    val duration: Duration,
    val status: AppointmentStatus,
) {

    val endDateTime: LocalDateTime = this.dateTime + this.duration

}