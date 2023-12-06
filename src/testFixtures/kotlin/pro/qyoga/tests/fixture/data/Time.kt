package pro.qyoga.tests.fixture.data

import java.time.Duration
import java.time.LocalDate
import kotlin.random.Random

fun randomMinutesDuration(minMinutes: Long = 1, maxMinutes: Long = Long.MAX_VALUE): Duration =
    Duration.ofMinutes(Random.nextLong(minMinutes, maxMinutes))

const val recentPeriod = 365L

fun randomRecentLocalDate() =
    randomLocalDate(LocalDate.now().minusDays(recentPeriod), Duration.ofDays(recentPeriod))

fun randomLocalDate(from: LocalDate, period: Duration): LocalDate =
    from.plusDays(Random.nextLong(0, period.toDays() + 1))