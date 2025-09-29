package pro.qyoga.i9ns.calendars.ical.commands

import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendar
import java.net.URL

data class CreateICalRq(
    val owner: TherapistRef,
    val icsUrl: URL,
    val name: String
)

fun ICalCalendar.Companion.createFrom(
    createICalRq: CreateICalRq,
    icsData: String
): ICalCalendar {
    val ical = ICalCalendar(
        createICalRq.owner,
        createICalRq.name,
        createICalRq.icsUrl,
        icsData
    )
    return ical
}
