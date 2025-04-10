package pro.qyoga.core.calendar.ical.platform.ical4j

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.data.ParserException
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.util.CompatibilityHints
import org.slf4j.LoggerFactory
import java.io.StringReader

private val log = LoggerFactory.getLogger(ICalIntegration::class.java)

fun tryParseIcs(icsData: String): Calendar? {
    CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true)
    val sin = StringReader(icsData.replace("\n", "\r\n"))
    val builder = CalendarBuilder()
    return try {
        builder.build(sin)
    } catch (e: ParserException) {
        log.error("ics-file parsing failed", e)
        null
    }
}