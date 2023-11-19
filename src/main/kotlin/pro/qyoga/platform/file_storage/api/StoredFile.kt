package pro.qyoga.platform.file_storage.api


class StoredFile(
    val metaData: FileMetaData,
    val content: ByteArray
)