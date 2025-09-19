package pro.qyoga.core.calendar.ical.model

import pro.qyoga.core.calendar.api.CalendarItemId


data class ICalEventId(
    val uid: String,
    val recurrenceId: String? = null
) : CalendarItemId {

    override fun toQueryParamStr(): String =
        "uid=${uid},rid=${recurrenceId ?: ""}"


}