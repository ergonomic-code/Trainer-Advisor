package nsu.fit.qyoga.cases.core.images.internal

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import nsu.fit.qyoga.cases.core.images.ImagesTestConfig
import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.multipart.MultipartFile
import org.testcontainers.shaded.org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream

@ContextConfiguration(
    classes = [ImagesTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
class ImagesServiceTests(
    @Autowired private val imageService: ImageService
) : QYogaModuleBaseTest() {

    private val file = File("src/test/resources/images/testImage.png")

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "db/image-init-script.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can save image and find them`() {
        val multipartFile = getMultipartFileFromInputStream(FileInputStream(file))
        val imageId = imageService.uploadImage(multipartFileToImage(multipartFile))
        val savedImage = imageService.getImage(imageId)
        savedImage shouldNotBe null
        savedImage?.id shouldBe imageId
        savedImage?.name shouldBe multipartFile.originalFilename
        savedImage?.size shouldBe multipartFile.size
        savedImage?.mediaType shouldBe "text/plain"
        savedImage?.data shouldBe multipartFile.bytes
    }

    @Test
    fun `QYoga can delete image`() {
        val multipartFile = getMultipartFileFromInputStream(FileInputStream(file))
        val imageId = imageService.uploadImage(multipartFileToImage(multipartFile))
        imageService.deleteImage(imageId)
        imageService.getImage(imageId) shouldBe null
    }

    @Test
    fun `QYoga can find image list`() {
        val multipartFile = getMultipartFileFromInputStream(FileInputStream(file))
        val fileByteArr = multipartFile.bytes
        imageService.uploadImage(multipartFileToImage(multipartFile))
        imageService.uploadImage(multipartFileToImage(multipartFile))
        val imageList = imageService.getImageList(listOf(1, 1, 2))
        imageList.size shouldBe 2
        for (image in imageList) {
            image.data shouldBe fileByteArr
            image.mediaType shouldBe "text/plain"
        }
    }

    @Test
    fun `QYoga can't find non-existing image`() {
        val multipartFile = getMultipartFileFromInputStream(FileInputStream(file))
        val id1 = imageService.uploadImage(multipartFileToImage(multipartFile))
        imageService.uploadImage(multipartFileToImage(multipartFile))
        val imageList = imageService.getImageList(listOf(id1, -1))
        imageList.size shouldBe 1
        imageList[0].id shouldBe id1
    }

    fun getMultipartFileFromInputStream(inputStream: FileInputStream): MultipartFile {
        return MockMultipartFile(
            "file",
            file.name,
            "text/plain",
            IOUtils.toByteArray(inputStream)
        )
    }

    fun multipartFileToImage(multipartFile: MultipartFile): Image {
        return Image(
            name = multipartFile.originalFilename ?: "",
            mediaType = multipartFile.contentType ?: "application/octet-stream",
            size = multipartFile.size,
            data = multipartFile.bytes
        )
    }
}
