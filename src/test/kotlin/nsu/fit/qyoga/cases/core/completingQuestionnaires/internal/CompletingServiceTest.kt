package nsu.fit.qyoga.cases.core.completingQuestionnaires.internal

import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.completingQuestionnaires.CompletingQuestionnairesTestConfig
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    classes = [CompletingQuestionnairesTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class CompletingServiceTest(
    @Autowired private val completingService: CompletingService,
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "db/completing/completing-questionnaires-init-script.sql" to "dataSource",
            "db/completing/insert_completing_data.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can retrieve questionnaires with different type of sort`() {
        val questionnairesASK = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(),
            PageRequest.of(0, 10, Sort.by("date").ascending())
        )
        val questionnairesDESK = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(),
            PageRequest.of(0, 10, Sort.by("date").descending())
        )
        questionnairesASK.content.size shouldBe 10
        questionnairesASK.content.map { it.id.toInt() } shouldBe listOf(6, 7, 8 , 9, 10, 11, 12, 13, 14, 15)
        questionnairesDESK.content.size shouldBe 10
        questionnairesDESK.content.map { it.id.toInt() } shouldBe listOf(5, 4, 3, 2, 1, 3, 15, 14, 13, 12)
    }

    /*@Test
    fun `QYoga can retrieve questionnaires without title`() {
        val questionnaires = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(title = null),
            PageRequest.of(0, 10, Sort.by("title").ascending())
        )
        questionnaires.content.size shouldBe 10
        questionnaires.content.map { it.id.toInt() } shouldBe listOf(2, 16, 17, 18, 6, 4, 8, 9, 12, 13)
    }

    @Test
    fun `QYoga can retrieve questionnaires page by page`() {
        val questionnairesPage1 = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(title = null),
            PageRequest.of(0, 10)
        )
        questionnairesPage1.content.size shouldBe 10
        val questionnairesPage2 = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(title = null),
            PageRequest.of(1, 10)
        )
        questionnairesPage2.content.size shouldBeLessThan 10
        questionnairesPage1.content.map {
            it.id.toInt()
        }.sorted() shouldNotBe questionnairesPage2.content.map {
            it.id.toInt()
        }.sorted()
        questionnairesPage1.content.map { it.id.toInt() }.plus(
            questionnairesPage2.content.map { it.id.toInt() }
        ).sorted() shouldBe (1..18).toList()
    }

    @Test
    fun `QYoga can retrieve questionnaires by title`() {
        val questionnairesTitle = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto("title"),
            PageRequest.of(0, 10)
        )
        questionnairesTitle.content.size shouldBe 0
        val questionnairesTest = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto("test"),
            PageRequest.of(0, 10)
        )
        questionnairesTest.content.size shouldBe 10
        for (questionnaire in questionnairesTest.content) {
            questionnaire.title shouldContain "test"
        }
    }*/
}
