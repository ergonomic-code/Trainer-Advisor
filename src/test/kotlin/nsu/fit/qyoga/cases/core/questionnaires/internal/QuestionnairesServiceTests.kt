package nsu.fit.qyoga.cases.core.questionnaires.internal

import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
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
        val questionnairesASK = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(),
            PageRequest.of(0, 10)
        )
        val questionnairesDESK = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(orderType = "DESK"),
            PageRequest.of(0, 10)
        )
        questionnairesASK.content.size shouldBe 10
        questionnairesASK.content.map { it.id.toInt() } shouldBe listOf(2, 16, 17, 18, 6, 4, 8, 9, 12, 13)
        questionnairesDESK.content.size shouldBe 10
        questionnairesDESK.content.map { it.id.toInt() } shouldBe listOf(10, 5, 1, 11, 7, 3, 15, 14, 13, 12)
    }

    @Test
    fun `QYoga can retrieve count of questionnaires without title`() {
        val questionnairesCount = questionnaireService.getQuestionnairesCount(null)
        questionnairesCount shouldBe 18
    }

    @Test
    fun `QYoga can retrieve count of questionnaires with title`() {
        val questionnairesCount = questionnaireService.getQuestionnairesCount("test")
        questionnairesCount shouldBe 12
    }

    @Test
    fun `QYoga can retrieve questionnaires without title`() {
        val questionnaires = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(),
            PageRequest.of(0, 10)
        )
        questionnaires.content.size shouldBe 10
        questionnaires.content.map { it.id.toInt() } shouldBe listOf(2, 16, 17, 18, 6, 4, 8, 9, 12, 13)
    }

    @Test
    fun `QYoga can retrieve questionnaires page by page`() {
        val questionnairesPage1 = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(),
            PageRequest.of(0, 10)
        )
        questionnairesPage1.content.size shouldBe 10
        val questionnairesPage2 = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(),
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
    }

    @Test
    fun `QYoga can save empty questionnaire`() {
        val createQuestionnaireDto = CreateQuestionnaireDto(
            title = "create questionnaire test",
            questions = emptyList()
        )
        val savedQuestionnaire = questionnaireService.createQuestionnaire(createQuestionnaireDto)
        val inDBQuestionnaire = questionnaireService.loadQuestionnairesWithQuestions(savedQuestionnaire.id)
        savedQuestionnaire.title shouldBe inDBQuestionnaire.title
        createQuestionnaireDto.title shouldBe inDBQuestionnaire.title
        inDBQuestionnaire.questions.size shouldBe 0
    }

    @Test
    fun `QYoga can save questionnaire with questions without image and answers`() {
        val questionnaireWithCreateQuestionDto = CreateQuestionnaireDto(
            title = "create questionnaire test",
            questions = listOf(
                CreateQuestionDto(text = null, questionType = QuestionType.TEXT, photo = null, answers = emptyList()),
                CreateQuestionDto(text = null, questionType = QuestionType.SEVERAL, photo = null, answers = emptyList()),
                CreateQuestionDto(text = "qTest", questionType = QuestionType.TEXT, photo = null, answers = emptyList())
            )
        )
        val savedQuestionnaire = questionnaireService.createQuestionnaire(questionnaireWithCreateQuestionDto)
        val inDBQuestionnaire = questionnaireService.loadQuestionnairesWithQuestions(savedQuestionnaire.id)
        savedQuestionnaire.title shouldBe inDBQuestionnaire.title
        inDBQuestionnaire.questions.size shouldBe 3
        for (question in inDBQuestionnaire.questions){
            question.answers.size shouldBe 0
        }
    }

    @Test
    fun `QYoga can save questionnaire with questions and answers without image`() {
        val questionnaireWithCreateQuestionDto = CreateQuestionnaireDto(
            title = "create questionnaire test",
            questions = listOf(
                CreateQuestionDto(
                    text = null,
                    questionType = QuestionType.TEXT,
                    photo = null,
                    answers = listOf(
                        CreateAnswerDto()
                    )
                )
            )
        )
        val savedQuestionnaire = questionnaireService.createQuestionnaire(questionnaireWithCreateQuestionDto)
        val inDBQuestionnaire = questionnaireService.loadQuestionnairesWithQuestions(savedQuestionnaire.id)
        savedQuestionnaire.title shouldBe inDBQuestionnaire.title
        inDBQuestionnaire.questions.size shouldBe 3
        for (question in inDBQuestionnaire.questions){
            question.answers.size shouldBe 0
        }
    }

}
