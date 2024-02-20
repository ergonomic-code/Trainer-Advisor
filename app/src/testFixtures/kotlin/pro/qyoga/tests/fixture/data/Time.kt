package pro.qyoga.tests.fixture.data

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.random.Random

fun randomMinutesDuration(minMinutes: Long = 1, maxMinutes: Long = Long.MAX_VALUE): Duration =
    Duration.ofMinutes(Random.nextLong(minMinutes, maxMinutes))

private const val recentPeriod = 365L

fun randomRecentLocalDate() =
    randomLocalDate(LocalDate.now().minusDays(recentPeriod), Duration.ofDays(recentPeriod))

fun randomLocalDate(from: LocalDate, period: Duration): LocalDate =
    from.plusDays(Random.nextLong(0, period.toDays() + 1))

fun randomTimeZone(): ZoneId = ZoneId.getAvailableZoneIds().random().let { ZoneId.of(it) }

val asiaNovosibirskTimeZone: ZoneId = ZoneId.of("Asia/Novosibirsk")

fun randomWorkingTime(): LocalTime {
    val hour = Random.nextInt(8, 21)
    val minute = Random.nextInt(12) * 5
    return LocalTime.of(hour, minute)
}
