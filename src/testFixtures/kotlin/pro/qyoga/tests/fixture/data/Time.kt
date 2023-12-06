package pro.qyoga.tests.fixture.data

import java.time.Duration
import java.time.LocalDate
import kotlin.random.Random

fun randomMinutesDuration(minMinutes: Long = 1, maxMinutes: Long = Long.MAX_VALUE): Duration =
    Duration.ofMinutes(Random.nextLong(minMinutes, maxMinutes))

fun randomLocalDate(from: LocalDate, period: Duration): LocalDate =
    from.plusDays(period.toDays())