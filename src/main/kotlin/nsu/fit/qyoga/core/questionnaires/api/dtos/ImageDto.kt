package nsu.fit.qyoga.core.questionnaires.api.dtos

import java.io.InputStream

data class ImageDto(
    val name: String?,
    val mediaType: String,
    val size: Long,
    val data: InputStream
)