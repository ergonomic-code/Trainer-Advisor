package nsu.fit.qyoga.cases.core.questionnaires.internal

import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.questionnaires.api.dtos.DecodingDto
import nsu.fit.qyoga.core.questionnaires.api.services.DecodingService
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
class DecodingServiceTest(
    @Autowired private val decodingService: DecodingService
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can create empty decoding by default`() {
        val decoding = decodingService.createNewDecoding(1)
        decoding.id shouldBe 1
        decoding.lowerBound shouldBe 0
        decoding.upperBound shouldBe 0
        decoding.result shouldBe ""
        decoding.questionnaireId shouldBe 1
    }

    @Test
    fun `QYoga can find decoding by questionnaire id`() {
        val decoding = decodingService.createNewDecoding(1)
        val inDbDecoding = decodingService.findDecodingByQuestionnaireId(1)
        inDbDecoding.size shouldBe 1
        decoding.id shouldBe inDbDecoding[0].id
        decoding.lowerBound shouldBe inDbDecoding[0].lowerBound
        decoding.upperBound shouldBe inDbDecoding[0].upperBound
        decoding.result shouldBe inDbDecoding[0].result
        decoding.questionnaireId shouldBe inDbDecoding[0].questionnaireId
    }

    @Test
    fun `QYoga can delete decoding by id`() {
        val decoding = decodingService.createNewDecoding(1)
        decodingService.deleteDecodingById(decoding.id)
        val decodingList = decodingService.findDecodingByQuestionnaireId(1)
        decodingList.size shouldBe 0
    }

    @Test
    fun `QYoga can save or update decoding`() {
        val decoding = decodingService.saveDecoding(DecodingDto(questionnaireId = 1))
        decoding.id shouldBe 1
        decoding.lowerBound shouldBe 0
        decoding.upperBound shouldBe 0
        decoding.result shouldBe ""
        decoding.questionnaireId shouldBe 1
        decoding.result = "test"
        val changedDecoding = decodingService.saveDecoding(decoding)
        changedDecoding.result shouldBe "test"
        decoding.id shouldBe changedDecoding.id
        decoding.lowerBound shouldBe changedDecoding.lowerBound
        decoding.upperBound shouldBe changedDecoding.upperBound
        decoding.questionnaireId shouldBe changedDecoding.questionnaireId
    }

    @Test
    fun `QYoga can save several decoding`() {
        val decoding = decodingService.saveDecodingList(
            listOf(
            DecodingDto(questionnaireId = 1),
            DecodingDto(questionnaireId = 1)
            )
        )
        val inDbDecoding = decodingService.findDecodingByQuestionnaireId(1)
        decoding.size shouldBe inDbDecoding.size

    }
}
