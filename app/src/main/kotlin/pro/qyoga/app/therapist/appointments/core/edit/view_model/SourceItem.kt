package pro.qyoga.app.therapist.appointments.core.edit.view_model

import pro.qyoga.core.calendar.ical.model.ICalCalendar
import pro.qyoga.core.calendar.ical.model.ICalEventId

data class SourceItem(
    val type: String,
    val id: String
) {

    companion object {
        fun icsEvent(eventId: ICalEventId): SourceItem =
            SourceItem(ICalCalendar.Companion.TYPE, eventId.toQueryParamStr())
    }

}

fun ICalEventId.toQueryParamStr(): String =
    "uid=${uid},rid=${recurrenceId ?: ""}"

fun SourceItem.icsEventId(): ICalEventId {
    check(type == ICalCalendar.TYPE)
    val matcher = "uid=(.+),rid=(.*)".toRegex().matchEntire(id)
    check(matcher != null)
    val uid = matcher.groups[1]!!.value
    val rid = matcher.groups[2]!!.value.takeIf { it.isNotBlank() }
    return ICalEventId(uid, rid)
}