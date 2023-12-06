package pro.qyoga.core.therapy.exercises.api

import pro.qyoga.infra.locale.qyogaLocale
import pro.qyoga.platform.java.time.toDecimalMinutes
import java.time.Duration

data class ExerciseDto(
    val id: Long,
    val title: String,
    val description: String,
    val duration: Duration,
    val type: ExerciseType,
) {

    val durationLabel = String.format(qyogaLocale, "%,.2f мин.", duration.toDecimalMinutes())

}