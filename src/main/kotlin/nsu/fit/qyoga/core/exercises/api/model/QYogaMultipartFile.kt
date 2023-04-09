package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream


class QYogaMultipartFile(private val image: Image) : MultipartFile {
    override fun getName(): String {
        return image.name
    }

    override fun getOriginalFilename(): String {
        return image.name
    }

    override fun getContentType(): String {
        return image.mediaType
    }

    override fun isEmpty(): Boolean {
        return image.data.isEmpty()
    }

    override fun getSize(): Long {
        return image.size
    }

    override fun getBytes(): ByteArray {
        return image.data
    }

    override fun transferTo(dest: File) {
    }

    override fun getInputStream(): InputStream {
        return image.data.inputStream()
    }
}