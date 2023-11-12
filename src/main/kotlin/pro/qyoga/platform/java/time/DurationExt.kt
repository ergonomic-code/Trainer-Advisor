package pro.qyoga.platform.java.time

import java.time.Duration
import kotlin.math.ceil
import kotlin.math.roundToLong

const val SECOND_PER_MINUTE = 60
fun Duration.toDecimalMinutes(): Double =
    this.toMinutes().toDouble() + (this.toSecondsPart().toDouble() / SECOND_PER_MINUTE)

fun Double.toDurationMinutes(): Duration {
    val minutes = ceil(this).toLong()
    val seconds = ((this - minutes) * SECOND_PER_MINUTE).roundToLong()
    return Duration.ofSeconds(minutes * SECOND_PER_MINUTE + seconds)
}
