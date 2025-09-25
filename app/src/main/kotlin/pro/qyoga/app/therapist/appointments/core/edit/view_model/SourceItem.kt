package pro.qyoga.app.therapist.appointments.core.edit.view_model

import pro.qyoga.core.calendar.api.CalendarItemId
import pro.qyoga.i9ns.calendars.google.GoogleCalendar
import pro.qyoga.i9ns.calendars.google.GoogleCalendarItemId
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendar
import pro.qyoga.i9ns.calendars.ical.model.ICalEventId

data class SourceItem(
    val type: String,
    val id: String
) {

    constructor(eventId: CalendarItemId) : this(eventId.type.name, eventId.toQueryParamStr())

    companion object {
        fun icsEvent(eventId: ICalEventId): SourceItem =
            SourceItem(ICalCalendar.Type.name, eventId.toQueryParamStr())

        fun googleEvent(eventId: GoogleCalendarItemId): SourceItem =
            SourceItem("Google", eventId.toQueryParamStr())

    }

}

fun SourceItem.icsEventId(): ICalEventId {
    check(type == ICalCalendar.Type.name)
    val matcher = "uid=(.+),rid=(.*)".toRegex().matchEntire(id)
    check(matcher != null)
    val uid = matcher.groups[1]!!.value
    val rid = matcher.groups[2]!!.value.takeIf { it.isNotBlank() }
    return ICalEventId(uid, rid)
}

fun SourceItem.googleEventId(): GoogleCalendarItemId {
    check(type == GoogleCalendar.Type.name)
    val matcher = "(.+),(.+)".toRegex().matchEntire(id)
    check(matcher != null)
    return GoogleCalendarItemId(matcher.groups[1]!!.value, matcher.groups[2]!!.value)
}
