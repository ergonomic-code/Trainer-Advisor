package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("images")
data class Image(
    @Id
    val id: Long = 0,
    val name: String,
    val mediaType: String,
    val size: Int,
    val data: ByteArray
)
