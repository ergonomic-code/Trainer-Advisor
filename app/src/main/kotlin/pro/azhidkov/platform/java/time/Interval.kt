package pro.azhidkov.platform.java.time

import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.Temporal


@ConsistentCopyVisibility
data class Interval<T : Temporal> private constructor(
    val from: T,
    val to: T
) {

    companion object {

        fun of(from: ZonedDateTime, duration: Duration): Interval<ZonedDateTime> = Interval(from, from + duration)

    }

}

val Interval<ZonedDateTime>.zoneId: ZoneId
    get() = from.zone