package pro.qyoga.i9ns.calendars.google.model

import pro.qyoga.core.calendar.api.Calendar
import pro.qyoga.core.calendar.api.CalendarType
import pro.qyoga.core.users.therapists.TherapistRef


typealias GoogleCalendarId = String

data class GoogleCalendar(
    override val ownerRef: TherapistRef,
    val externalId: GoogleCalendarId,
    override val name: String,
) : Calendar {

    override val type: CalendarType = Type

    object Type : CalendarType {
        override val name = "Google"
    }

}
