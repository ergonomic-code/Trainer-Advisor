package nsu.fit.qyoga.cases.core.questionnaires.internal


import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.questionnaires.api.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.utils.OrderType
import nsu.fit.qyoga.core.questionnaires.utils.Page
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
class QuestionnairesServiceTests(
    @Autowired private val questionnaireService: QuestionnaireService
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "/db/questionnaires/questionnaires-insert-data-script.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can retrieve questionnaires with different type of sort`() {
        val questionnairesASK = questionnaireService.findQuestionnaires(null, Page())
        val questionnairesDESK = questionnaireService.findQuestionnaires(null, Page(orderType = OrderType.DESK))
        questionnairesASK.size shouldBe 10
        questionnairesDESK.size shouldBe 10
    }

    @Test
    fun `QYoga can retrieve count of questionnaires without title`() {
        val questionnairesCount =questionnaireService.getQuestionnairesCount(null)
        questionnairesCount shouldBe 12
    }

    @Test
    fun `QYoga can retrieve count of questionnaires with title`() {
        val questionnairesCount =questionnaireService.getQuestionnairesCount("test")
        questionnairesCount shouldBe 6
    }

    @Test
    fun `QYoga can retrieve questionnaires without title`() {
        val questionnaires = questionnaireService.findQuestionnaires(null, Page())
        questionnaires.size shouldBe 10
    }

    @Test
    fun `QYoga can retrieve questionnaires page by page`() {
        val questionnairesPage1 = questionnaireService.findQuestionnaires(null, Page())
        questionnairesPage1.size shouldBe 10
        val questionnairesPage2 = questionnaireService.findQuestionnaires(null, Page(pageNum = 2))
        questionnairesPage2.size shouldBeLessThan 10
    }

    @Test
    fun `QYoga can retrieve questionnaires by title`() {
        val questionnaires = questionnaireService.findQuestionnaires("test", Page())
        questionnaires.size shouldBe 6
    }

}
