package pro.azhidkov.platform.postgresql

import org.postgresql.util.PGInterval
import java.time.Duration

fun PGInterval.toDuration(): Duration = Duration.ofDays(this.days.toLong())
    .plusHours(this.hours.toLong())
    .plusMinutes(this.minutes.toLong())
    .plusSeconds(this.seconds.toLong())

fun Duration.toPGInterval() = PGInterval(this.toString())