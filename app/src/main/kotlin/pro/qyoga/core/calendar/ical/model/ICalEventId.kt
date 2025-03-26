package pro.qyoga.core.calendar.ical.model


data class ICalEventId(
    val uid: String,
    val recurrenceId: String?
)