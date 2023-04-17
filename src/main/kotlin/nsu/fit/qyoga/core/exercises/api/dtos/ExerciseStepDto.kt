package nsu.fit.qyoga.core.exercises.api.dtos

import org.springframework.web.multipart.MultipartFile

class ExerciseStepDto(
    var imageId: Long?,
    var description: String,
    var photo: MultipartFile?
) {
    constructor() : this(0, "", null)
}
