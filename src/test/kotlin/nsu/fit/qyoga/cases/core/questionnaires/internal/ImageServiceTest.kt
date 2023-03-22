package nsu.fit.qyoga.cases.core.questionnaires.internal

import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.questionnaires.api.services.ImageService
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

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
    fun `QYoga can save image`() {

    }
}