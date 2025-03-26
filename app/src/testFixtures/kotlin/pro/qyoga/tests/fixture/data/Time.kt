package pro.qyoga.tests.fixture.data

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

fun randomMinutesDuration(minMinutes: Long = 1, maxMinutes: Long = Long.MAX_VALUE): Duration =
    Duration.ofMinutes(faker.random().nextLong(minMinutes, maxMinutes))

private const val recentPeriod = 365L

fun randomRecentLocalDate() =
    randomLocalDate(LocalDate.now().minusDays(recentPeriod), Duration.ofDays(recentPeriod))

fun randomLocalDate(from: LocalDate, period: Duration): LocalDate =
    from.plusDays(faker.random().nextLong(0, period.toDays() + 1))

fun randomTimeZone(): ZoneId = ZoneId
    .getAvailableZoneIds()
    .map { ZoneId.of(it) }
    // Быстрый воркэрануд для того, что ical зачем-то подсовывает сюда пачку непарсибальных зон из-за которых падают тесты
    .filter { "ical" !in it.id }
    .let { faker.random().randomElementOf(it) }

val asiaNovosibirskTimeZone: ZoneId = ZoneId.of("Asia/Novosibirsk")

fun randomWorkingTime(): LocalTime {
    val hour = faker.random().nextInt(8, 21)
    val minute = faker.random().nextInt(12) * 5
    return LocalTime.of(hour, minute)
}
