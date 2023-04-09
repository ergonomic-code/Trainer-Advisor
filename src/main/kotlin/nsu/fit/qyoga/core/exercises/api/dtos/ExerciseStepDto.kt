package nsu.fit.qyoga.core.exercises.api.dtos

import org.springframework.web.multipart.MultipartFile

class ExerciseStepDto(
    var description: String,
    var photo: MultipartFile?
) {
    constructor() : this("", null)
}
