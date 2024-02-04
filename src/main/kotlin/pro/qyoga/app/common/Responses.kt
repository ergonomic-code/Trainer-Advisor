package pro.qyoga.app.common

import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.file_storage.api.StoredFileInputStream


val notFound = ModelAndView("forward:error/404")

object ResponseEntityExt {

    fun ok(storedFileInputStream: StoredFileInputStream): ResponseEntity<InputStreamResource> =
        ResponseEntity.ok()
            .headers {
                it.contentType = MediaType.parseMediaType(storedFileInputStream.metaData.mediaType)
                it.contentLength = storedFileInputStream.metaData.size
                it.contentDisposition = ContentDispositionExt.inline(storedFileInputStream.metaData)
            }
            .body(InputStreamResource(storedFileInputStream.inputStream))

}