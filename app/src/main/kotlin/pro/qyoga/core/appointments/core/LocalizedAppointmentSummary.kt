package pro.qyoga.core.appointments.core

import pro.qyoga.core.calendar.LocalCalendarItem
import java.time.Duration
import java.time.LocalDateTime
import java.util.*


data class LocalizedAppointmentSummary(
    override val id: UUID,
    val clientName: String,
    val typeName: String,
    override val dateTime: LocalDateTime,
    override val duration: Duration,
    val status: AppointmentStatus,
) : LocalCalendarItem<UUID> {

    override val endDateTime: LocalDateTime = this.dateTime + this.duration

    override val title: String = clientName

    override val description: String = typeName

}