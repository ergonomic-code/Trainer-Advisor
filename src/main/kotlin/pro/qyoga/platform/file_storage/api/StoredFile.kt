package pro.qyoga.platform.file_storage.api

import org.springframework.data.annotation.Id


class StoredFile(
    val metaData: FileMetaData,
    val content: ByteArray
) {

    @Id
    val id: Long = metaData.id

}