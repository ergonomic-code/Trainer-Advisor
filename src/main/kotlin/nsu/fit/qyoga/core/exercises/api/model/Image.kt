package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("images")
data class Image(
    @Id
    val id: Long = 0,
    val name: String,
    val mediaType: String,
    val size: Long,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (id != other.id) return false
        if (name != other.name) return false
        if (mediaType != other.mediaType) return false
        if (size != other.size) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + mediaType.hashCode()
        result = (31 * result + size).toInt()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
