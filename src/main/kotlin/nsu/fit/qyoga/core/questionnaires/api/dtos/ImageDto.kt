package nsu.fit.qyoga.core.questionnaires.api.dtos

data class ImageDto(
    val id: Long = 0,
    val name: String,
    val mediaType: String,
    val size: Long,
    val data: ByteArray
) : java.io.Serializable
