package pro.qyoga.app.therapist.appointments.core.edit.view_model

import pro.qyoga.core.calendar.google.GoogleCalendar
import pro.qyoga.core.calendar.google.GoogleCalendarItemId
import pro.qyoga.core.calendar.ical.model.ICalCalendar
import pro.qyoga.core.calendar.ical.model.ICalEventId

data class SourceItem(
    val type: String,
    val id: String
) {

    companion object {
        fun icsEvent(eventId: ICalEventId): SourceItem =
            SourceItem(ICalCalendar.TYPE, eventId.toQueryParamStr())

        fun googleEvent(eventId: GoogleCalendarItemId): SourceItem =
            SourceItem("Google", eventId.toQueryParamStr())

    }

}

fun SourceItem.icsEventId(): ICalEventId {
    check(type == ICalCalendar.TYPE)
    val matcher = "uid=(.+),rid=(.*)".toRegex().matchEntire(id)
    check(matcher != null)
    val uid = matcher.groups[1]!!.value
    val rid = matcher.groups[2]!!.value.takeIf { it.isNotBlank() }
    return ICalEventId(uid, rid)
}

fun SourceItem.googleEventId(): GoogleCalendarItemId {
    check(type == GoogleCalendar.TYPE)
    val matcher = "(.+),(.+)".toRegex().matchEntire(id)
    check(matcher != null)
    return GoogleCalendarItemId(matcher.groups[1]!!.value, matcher.groups[2]!!.value)
}