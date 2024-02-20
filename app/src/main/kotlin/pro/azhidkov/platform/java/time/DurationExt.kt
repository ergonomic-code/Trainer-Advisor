package pro.azhidkov.platform.java.time

import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.roundToLong

const val SECOND_PER_MINUTE = 60

fun Duration?.toDecimalMinutes(): Double? =
    if (this == null) {
        null
    } else {
        this.toMinutes().toDouble() + (this.toSecondsPart().toDouble() / SECOND_PER_MINUTE)
    }

fun Double.toDurationMinutes(): Duration {
    val minutes = ceil(this).toLong()
    val seconds = ((this - minutes) * SECOND_PER_MINUTE).roundToLong()
    return Duration.ofSeconds(minutes * SECOND_PER_MINUTE + seconds)
}

fun Duration?.toLocalTimeString() =
    this?.let {
        DateTimeFormatter.ISO_TIME.format(LocalTime.ofSecondOfDay(toSeconds()))
    }
