package pro.qyoga.core.calendar.gateways

import org.springframework.stereotype.Service
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarItemId
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.calendar.api.SourceItem
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.ZonedDateTime

@Service
class CalendarItemsResolver(
    calendarsServices: List<CalendarsService<*>>
) {

    private val services = calendarsServices.associateBy { it.type.name }

    fun findCalendarItem(
        therapistRef: TherapistRef,
        sourceItem: SourceItem
    ): CalendarItem<CalendarItemId, ZonedDateTime>? {
        @Suppress("UNCHECKED_CAST")
        val service = services[sourceItem.type] as CalendarsService<CalendarItemId>
        val id = service.parseStringId(sourceItem)
        return service.findById(therapistRef, id)
    }

}
