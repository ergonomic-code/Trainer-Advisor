package pro.azhidkov.platform.file_storage.api

import java.io.InputStream


class StoredFileInputStream(
    val metaData: FileMetaData,
    val inputStream: InputStream
)