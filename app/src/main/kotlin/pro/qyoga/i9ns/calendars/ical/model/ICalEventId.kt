package pro.qyoga.i9ns.calendars.ical.model

import pro.qyoga.core.calendar.api.CalendarItemId


data class ICalEventId(
    val uid: String,
    val recurrenceId: String? = null
) : CalendarItemId {

    override val type: String = ICalCalendar.TYPE

    override fun toQueryParamStr(): String =
        "uid=${uid},rid=${recurrenceId ?: ""}"


}
