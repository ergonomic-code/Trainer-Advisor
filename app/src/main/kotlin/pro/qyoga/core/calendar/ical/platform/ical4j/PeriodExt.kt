package pro.qyoga.core.calendar.ical.platform.ical4j

import net.fortuna.ical4j.model.Period
import pro.azhidkov.platform.java.time.Interval
import java.time.temporal.Temporal


fun <T : Temporal> Interval<T>.toICalPeriod() =
    Period(from, to)