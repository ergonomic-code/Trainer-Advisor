package pro.qyoga.core.calendar.ical

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.calendar.ical.commands.CreateICalRq
import pro.qyoga.core.calendar.ical.commands.createFrom
import pro.qyoga.core.calendar.ical.model.*
import pro.qyoga.core.calendar.ical.persistance.ICalCalendarsDao
import pro.qyoga.core.calendar.ical.persistance.findAllByOwner
import pro.qyoga.core.calendar.ical.platform.ical4j.toICalCalendarItem
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDateTime
import java.time.ZonedDateTime


@Component
class ICalCalendarsRepo(
    private val iCalCalendarsDao: ICalCalendarsDao
) : CalendarsService {

    private val log = LoggerFactory.getLogger(javaClass)

    fun addICal(createICalRq: CreateICalRq): ICalCalendar {
        val icsData = createICalRq.icsUrl.readText()
        val ical = ICalCalendar.createFrom(createICalRq, icsData)
        return iCalCalendarsDao.save(ical)
    }

    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): Iterable<CalendarItem<*, LocalDateTime>> {
        val res = iCalCalendarsDao
            .findAllByOwner(therapist)
            .flatMap { ical -> ical.localizedICalCalendarItemsIn(interval) }

        return res
    }

    fun findById(therapist: TherapistRef, icsEventId: ICalEventId): CalendarItem<ICalEventId, ZonedDateTime>? {
        return iCalCalendarsDao.findAllByOwner(therapist)
            .asSequence()
            .mapNotNull { it.findById(icsEventId) }
            .firstOrNull()
            ?.toICalCalendarItem()
    }


    @Scheduled(cron = "0 */10 * * * *")
    fun sync() {
        log.info("Syncing calendars")
        iCalCalendarsDao.forAll {
            Sync.syncCalendar(iCalCalendarsDao, it)
        }
    }

}

private fun ICalCalendar.localizedICalCalendarItemsIn(
    interval: Interval<ZonedDateTime>,
): List<LocalizedICalCalendarItem> =
    this.calendarItemsIn(interval)
        .map(ICalCalendarItem::toLocalizedICalCalendarItem)