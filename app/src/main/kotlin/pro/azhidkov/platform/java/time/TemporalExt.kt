package pro.azhidkov.platform.java.time

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.Temporal

fun Temporal.toLocalDateTime(zoneId: ZoneId): LocalDateTime = when (this) {
    is LocalDateTime -> this
    is OffsetDateTime -> this.atZoneSameInstant(zoneId).toLocalDateTime()
    is ZonedDateTime -> this.withZoneSameInstant(zoneId).toLocalDateTime()
    else -> error("Unsupported type: $this")
}