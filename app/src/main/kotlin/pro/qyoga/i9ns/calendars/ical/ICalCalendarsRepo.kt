package pro.qyoga.i9ns.calendars.ical

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.calendar.api.*
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.ical.commands.CreateICalRq
import pro.qyoga.i9ns.calendars.ical.commands.createFrom
import pro.qyoga.i9ns.calendars.ical.ical4j.toICalCalendarItem
import pro.qyoga.i9ns.calendars.ical.model.*
import pro.qyoga.i9ns.calendars.ical.persistance.ICalCalendarsDao
import pro.qyoga.i9ns.calendars.ical.persistance.findAllByOwner
import java.time.ZonedDateTime


@Component
class ICalCalendarsRepo(
    private val iCalCalendarsDao: ICalCalendarsDao
) : CalendarsService<ICalEventId> {

    private val log = LoggerFactory.getLogger(javaClass)

    override val type: CalendarType = ICalCalendar.Type

    fun addICal(createICalRq: CreateICalRq): ICalCalendar {
        val icsData = createICalRq.icsUrl.readText()
        val ical = ICalCalendar.createFrom(createICalRq, icsData)
        return iCalCalendarsDao.save(ical)
    }

    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): SearchResult<ICalEventId> {
        val res = iCalCalendarsDao
            .findAllByOwner(therapist)
            .flatMap { ical -> ical.localizedICalCalendarItemsIn(interval) }

        return SearchResult(res)
    }

    override fun findById(
        therapistRef: TherapistRef,
        eventId: ICalEventId
    ): CalendarItem<ICalEventId, ZonedDateTime>? {
        return iCalCalendarsDao.findAllByOwner(therapistRef)
            .asSequence()
            .mapNotNull { it.findById(eventId) }
            .firstOrNull()
            ?.toICalCalendarItem()
    }

    override fun parseStringId(sourceItem: SourceItem): ICalEventId =
        sourceItem.icsEventId()


    @Scheduled(cron = "0 */10 * * * *")
    fun sync() {
        log.info("Syncing ical calendars")
        iCalCalendarsDao.forAll {
            Sync.syncCalendar(iCalCalendarsDao, it)
        }
    }

}

private fun ICalCalendar.localizedICalCalendarItemsIn(
    interval: Interval<ZonedDateTime>,
): List<LocalizedICalCalendarItem> =
    (this.calendarItemsIn(interval) ?: emptyList())
        .map(ICalCalendarItem::toLocalizedICalCalendarItem)

fun SourceItem.icsEventId(): ICalEventId {
    check(type == ICalCalendar.Type.name)
    val matcher = "uid=(.+),rid=(.*)".toRegex().matchEntire(id)
    check(matcher != null)
    val uid = matcher.groups[1]!!.value
    val rid = matcher.groups[2]!!.value.takeIf { it.isNotBlank() }
    return ICalEventId(uid, rid)
}
