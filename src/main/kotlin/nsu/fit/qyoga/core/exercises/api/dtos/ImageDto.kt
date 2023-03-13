package nsu.fit.qyoga.core.exercises.api.dtos

import java.io.InputStream

data class ImageDto(
    val name: String,
    val mediaType: String,
    val size: Int,
    val data: InputStream
)
