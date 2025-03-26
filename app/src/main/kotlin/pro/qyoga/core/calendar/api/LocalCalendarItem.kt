package pro.qyoga.core.calendar.api

import java.time.Duration
import java.time.LocalDateTime

interface LocalCalendarItem<ID> {
    val id: ID
    val title: String
    val description: String
    val dateTime: LocalDateTime
    val duration: Duration
    val endDateTime: LocalDateTime
        get() = this.dateTime.plus(this.duration)
}