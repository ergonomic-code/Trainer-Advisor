package pro.qyoga.i9ns.calendars.google

import pro.qyoga.core.calendar.api.Calendar
import pro.qyoga.core.users.therapists.TherapistRef


data class GoogleCalendar(
    override val ownerRef: TherapistRef,
    val externalId: String,
    override val name: String,
) : Calendar {

    override val type: String = TYPE

    companion object {

        const val TYPE = "Google"
    }

}
