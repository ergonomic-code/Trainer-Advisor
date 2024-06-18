package pro.qyoga.tests.fixture.object_mothers

import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import pro.azhidkov.platform.file_storage.api.FileMetaData
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.qyoga.tests.fixture.data.*
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.random.Random


object FilesObjectMother {

    fun randomFile(
        name: String = randomFileBaseName(),
        format: String = randomFileExtension(),
        size: Int = randomTestFileSize()
    ): StoredFile {
        val data = Random.nextBytes(size)
        val fileName = fileName(name, format)
        return StoredFile(
            FileMetaData(
                fileName, Files.probeContentType(Path.of(fileName)), data.size.toLong()
            ), data
        )
    }

    fun randomImage(
        name: String = randomFileBaseName(),
        format: String = randomImageExtension()
    ): StoredFile {
        val img = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
        val out = ByteArrayOutputStream()
        ImageIO.write(img, format, out)
        val data = out.toByteArray()

        val fileName = fileName(name, format)

        return StoredFile(
            FileMetaData(
                fileName, Files.probeContentType(Path.of(fileName)), data.size.toLong()
            ), data
        )
    }


    fun randomFileAsMultipartFile(
        name: String = randomFileBaseName(),
        format: String = randomFileExtension(),
        size: Int = randomTestFileSize()
    ): MultipartFile {
        val img = randomImage(name, format)
        return MockMultipartFile(img.metaData.name, img.metaData.name, img.metaData.mediaType, img.content)
    }


    fun randomImageAsMultipartFile(
        name: String = randomFileBaseName(), format: String = randomImageExtension()
    ): MultipartFile = randomFileAsMultipartFile(name, format)

}