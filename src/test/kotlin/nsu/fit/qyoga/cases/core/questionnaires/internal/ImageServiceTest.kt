package nsu.fit.qyoga.cases.core.questionnaires.internal

import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.questionnaires.api.errors.ImageException
import nsu.fit.qyoga.core.questionnaires.api.services.ImageService
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.Assertions
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
    classes = [QuestionnairesTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
class ImageServiceTest(
    @Autowired private val imageService: ImageService
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can save image and find them`() {
        val file = File("src/test/resources/db/questionnaires/testLargeImage.jpg")
        val input = FileInputStream(file)
        val multipartFile: MultipartFile = MockMultipartFile(
            "file",
            file.name,
            "text/plain",
            IOUtils.toByteArray(input)
        )
        val imageId = imageService.uploadImage(multipartFile)
        val savedImage = imageService.getImage(imageId)
        savedImage.id shouldBe imageId
        savedImage.name shouldBe multipartFile.originalFilename
        savedImage.size shouldBe multipartFile.size
        savedImage.mediaType shouldBe "text/plain"
        savedImage.data shouldBe multipartFile.bytes
    }

    @Test
    fun `QYoga can delete image`() {
        val file = File("src/test/resources/db/questionnaires/testLargeImage.jpg")
        val input = FileInputStream(file)
        val multipartFile: MultipartFile = MockMultipartFile(
            "file",
            file.name,
            "text/plain",
            IOUtils.toByteArray(input)
        )
        val imageId = imageService.uploadImage(multipartFile)
        imageService.deleteImage(imageId)
        val thrown1: ImageException = Assertions.assertThrows(
            ImageException::class.java
        ) { imageService.getImage(imageId) }
        Assertions.assertEquals("Выбранное изображение не найдено", thrown1.message)
    }
}
