package pro.qyoga.app.common

import org.springframework.http.ContentDisposition
import pro.qyoga.platform.file_storage.api.FileMetaData
import java.nio.charset.Charset


object ContentDispositionExt {

    fun inline(fileMetaData: FileMetaData): ContentDisposition =
        ContentDisposition.inline()
            .filename(fileMetaData.name, Charset.defaultCharset())
            .build()

    fun inline(fileName: String): ContentDisposition =
        ContentDisposition.inline()
            .filename(fileName, Charset.defaultCharset())
            .build()

}