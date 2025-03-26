package pro.qyoga.core.calendar.ical.platform.ical4j

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.util.CompatibilityHints
import java.io.StringReader


fun parseIcs(icsData: String): Calendar {
    CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true)
    val sin = StringReader(icsData)
    val builder = CalendarBuilder()
    return builder.build(sin)
}