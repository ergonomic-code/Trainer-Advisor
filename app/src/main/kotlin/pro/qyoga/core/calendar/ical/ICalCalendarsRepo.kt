package pro.qyoga.core.calendar.ical

import net.fortuna.ical4j.model.Period
import net.fortuna.ical4j.model.component.VEvent
import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.calendar.api.LocalCalendarItem
import pro.qyoga.core.calendar.ical.commands.CreateICalRq
import pro.qyoga.core.calendar.ical.commands.createFrom
import pro.qyoga.core.calendar.ical.model.*
import pro.qyoga.core.calendar.ical.persistance.ICalCalendarsDao
import pro.qyoga.core.calendar.ical.persistance.findAllByOwner
import pro.qyoga.core.calendar.ical.platform.ical4j.toICalPeriod
import pro.qyoga.core.calendar.ical.platform.ical4j.toLocalizedICalCalendarItem
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.ZonedDateTime
import java.time.temporal.Temporal


@Component
class ICalCalendarsRepo(
    private val iCalCalendarsDao: ICalCalendarsDao
) : CalendarsService {

    fun addICal(createICalRq: CreateICalRq): ICalCalendar {
        val icsData = createICalRq.icsUrl.readText()
        val ical = ICalCalendar.createFrom(createICalRq, icsData)
        return iCalCalendarsDao.save(ical)
    }

    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): Iterable<LocalCalendarItem<*>> {
        val searchPeriod = interval.toICalPeriod()

        val res = iCalCalendarsDao
            .findAllByOwner(therapist)
            .flatMap { ical -> ical.localizedICalCalendarItemsIn(searchPeriod) }

        return res
    }

    fun findById(therapist: TherapistRef, icsEventId: ICalEventId): VEvent? {
        return iCalCalendarsDao.findAllByOwner(therapist)
            .asSequence()
            .mapNotNull { it.findById(icsEventId) }
            .firstOrNull()
    }

}

private fun ICalCalendar.localizedICalCalendarItemsIn(
    searchPeriod: Period<ZonedDateTime>,
): List<LocalizedICalCalendarItem> =
    vEventOccurrencesIn(searchPeriod)
        .map { (vEvent, vEventPeriod: Period<Temporal>) ->
            vEvent.toLocalizedICalCalendarItem(vEventPeriod, searchPeriod.start.zone)
        }
