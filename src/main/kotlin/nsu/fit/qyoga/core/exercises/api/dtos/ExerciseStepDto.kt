package nsu.fit.qyoga.core.exercises.api.dtos

import org.springframework.web.multipart.MultipartFile

data class ExerciseStepDto(
    val description: String,
    val photo: MultipartFile?
)