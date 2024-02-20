package pro.azhidkov.platform.file_storage.api

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.erpo.hydration.Identifiable
import java.time.Instant

@Table("files")
data class FileMetaData(
    val name: String,
    val mediaType: String,
    val size: Long,

    val bucket: String = "",

    @Id
    override val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) : Identifiable<Long> {

    fun atBucket(bucket: String) = copy(bucket = bucket)

}
