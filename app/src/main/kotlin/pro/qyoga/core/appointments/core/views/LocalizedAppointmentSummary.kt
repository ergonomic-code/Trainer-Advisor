package pro.qyoga.core.appointments.core.views

import pro.qyoga.core.appointments.core.model.AppointmentStatus
import pro.qyoga.core.calendar.api.CalendarItem
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

data class LocalizedAppointmentSummary(
    override val id: UUID,
    val clientName: String,
    val typeName: String,
    override val dateTime: LocalDateTime,
    override val duration: Duration,
    val status: AppointmentStatus
) : CalendarItem<UUID, LocalDateTime> {

    override val endDateTime: LocalDateTime = this.dateTime + this.duration

    override val title: String = clientName

    override val description: String = typeName

    override val location: String? = null

}