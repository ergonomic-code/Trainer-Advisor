package pro.qyoga.i9ns.calendars.ical.model

import pro.azhidkov.platform.kotlin.mapOfNotNull
import pro.qyoga.core.calendar.api.CalendarItemId
import pro.qyoga.core.calendar.api.CalendarType


data class ICalEventId(
    val uid: String,
    val recurrenceId: String? = null
) : CalendarItemId {

    override val type: CalendarType = ICalCalendar.Type

    override fun toMap(): Map<String, String> = mapOfNotNull(
        "uid" to uid,
        recurrenceId?.let { "rid" to recurrenceId }
    )

}
