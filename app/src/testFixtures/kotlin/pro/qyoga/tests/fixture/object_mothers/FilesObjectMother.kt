package pro.qyoga.tests.fixture.object_mothers

import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import pro.azhidkov.platform.file_storage.api.FileMetaData
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.qyoga.tests.fixture.data.*
import pro.qyoga.tests.platform.data_faker.randomAwtColor
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO


object FilesObjectMother {

    fun randomFile(
        name: String = randomFileBaseName(),
        format: String = randomFileExtension(),
        size: Int = randomTestFileSize()
    ): StoredFile {
        val data = faker.random().nextRandomBytes(size)
        val fileName = fileName(name, format)
        return StoredFile(
            FileMetaData(
                fileName, Files.probeContentType(Path.of(fileName)), data.size.toLong()
            ), data
        )
    }

    fun randomImageFile(
        name: String = randomFileBaseName(),
        format: String = randomImageExtension()
    ): StoredFile {
        val data = randomImage(format)

        val fileName = fileName(name, format)

        return StoredFile(
            FileMetaData(
                fileName, Files.probeContentType(Path.of(fileName)), data.size.toLong()
            ), data
        )
    }

    private fun randomImage(format: String, width: Int = 10, height: Int = 10): ByteArray {
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = img.createGraphics()
        g.paint = faker.color().randomAwtColor()
        g.fillRect(0, 0, width, height)

        faker.color()
        val out = ByteArrayOutputStream()
        ImageIO.write(img, format, out)
        return out.toByteArray()
    }

    private fun randomFileAsMultipartFile(
        name: String = randomFileBaseName(),
        format: String = randomFileExtension()
    ): MultipartFile {
        val img = randomImageFile(name, format)
        return MockMultipartFile(img.metaData.name, img.metaData.name, img.metaData.mediaType, img.content)
    }


    fun randomImageAsMultipartFile(
        name: String = randomFileBaseName(), format: String = randomImageExtension()
    ): MultipartFile = randomFileAsMultipartFile(name, format)

}