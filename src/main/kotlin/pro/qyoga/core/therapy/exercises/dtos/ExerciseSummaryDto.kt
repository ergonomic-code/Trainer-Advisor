package pro.qyoga.core.therapy.exercises.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import pro.qyoga.core.therapy.exercises.model.ExerciseType
import java.time.Duration

data class ExerciseSummaryDto(
    val title: String,

    val description: String,

    @field:JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "MINUTES")
    val duration: Duration,

    val type: ExerciseType,

    val id: Long = 0
)