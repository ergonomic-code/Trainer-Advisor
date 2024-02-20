package pro.qyoga.app.platform

import org.springframework.web.multipart.MultipartFile
import pro.azhidkov.platform.file_storage.api.FileMetaData
import pro.azhidkov.platform.file_storage.api.StoredFile


fun MultipartFile.toStoredFile() =
    StoredFile(FileMetaData(this.originalFilename!!, this.contentType!!, this.size), this.bytes)
