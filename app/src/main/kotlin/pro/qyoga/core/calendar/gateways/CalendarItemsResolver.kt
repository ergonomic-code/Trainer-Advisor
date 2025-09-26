package pro.qyoga.core.calendar.gateways

import org.springframework.stereotype.Service
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarItemId
import pro.qyoga.core.calendar.api.CalendarItemUri
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.users.therapists.TherapistRef
import java.net.URI
import java.time.ZonedDateTime

@Service
class CalendarItemsResolver(
    calendarsServices: List<CalendarsService<*>>
) {

    private val services = calendarsServices.associateBy { it.type.name }

    fun findCalendarItem(
        therapistRef: TherapistRef,
        sourceItem: URI
    ): CalendarItem<CalendarItemId, ZonedDateTime>? {
        val calendarItemUri = CalendarItemUri.parseOrNull(sourceItem)
            ?: throw IllegalArgumentException("Invalid calendar item URI: $sourceItem")

        @Suppress("UNCHECKED_CAST")
        val service = services[calendarItemUri.type] as CalendarsService<CalendarItemId>
        val id = service.createItemId(calendarItemUri.params)
        return service.findById(therapistRef, id)
    }

}
