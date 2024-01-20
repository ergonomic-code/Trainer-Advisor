package pro.qyoga.app.common

import org.springframework.web.multipart.MultipartFile
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.StoredFile


fun MultipartFile.toStoredFile() =
    StoredFile(FileMetaData(this.originalFilename!!, this.contentType!!, this.size), this.bytes)
