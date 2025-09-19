package pro.qyoga.core.calendar.api

import java.time.Duration
import java.time.temporal.Temporal

interface CalendarItemId {

    fun toQueryParamStr(): String

}

interface CalendarItem<ID, DATE : Temporal> {
    val id: ID
    val title: String
    val description: String
    val dateTime: DATE
    val duration: Duration

    @Suppress("UNCHECKED_CAST")
    val endDateTime: DATE
        get() = this.dateTime.plus(this.duration) as DATE
    val location: String?
}