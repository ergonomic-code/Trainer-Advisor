package pro.qyoga.i9ns.calendars.ical

import org.slf4j.LoggerFactory
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendar
import pro.qyoga.i9ns.calendars.ical.persistance.ICalCalendarsDao
import java.io.IOException


object Sync {

    private val log = LoggerFactory.getLogger(javaClass)

    fun syncCalendar(iCalCalendarsDao: ICalCalendarsDao, calendar: ICalCalendar) {
        log.info("Syncing {}", calendar.id)
        val icsFile = tryReadSourceUrl(calendar)
            ?: return

        val updatedICal = calendar.withIcsFile(icsFile)

        iCalCalendarsDao.save(updatedICal)
    }

    private fun tryReadSourceUrl(calendar: ICalCalendar): String? {
        return try {
            calendar.icsUrl.readText()
        } catch (ex: IOException) {
            log.debug("Calendar {} sync failed", calendar.id, ex)
            null
        }
    }

}
