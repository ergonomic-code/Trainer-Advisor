package nsu.fit.qyoga.cases.core.questionnaires.internal

import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
class QuestionnairesServiceTest(
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
            QuestionnaireSearchDto(title = null),
            PageRequest.of(0, 10, Sort.by("title").ascending())
        )
        val questionnairesDESK = questionnaireService.findQuestionnaires(
            QuestionnaireSearchDto(title = null),
            PageRequest.of(0, 10, Sort.by("title").descending())
        )
        questionnairesASK.content.size shouldBe 10
        questionnairesASK.content.map { it.id.toInt() } shouldBe listOf(2, 16, 17, 18, 6, 4, 8, 9, 12, 13)
        questionnairesDESK.content.size shouldBe 10
        questionnairesDESK.content.map { it.id.toInt() } shouldBe listOf(10, 5, 1, 11, 7, 3, 15, 14, 13, 12)
    }

    @Test
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
    }

    @Test
    fun `QYoga can save empty questionnaire`() {
        val createQuestionnaireDto = CreateQuestionnaireDto(
            id = 0,
            title = "create questionnaire test",
            questions = mutableListOf()
        )
        val savedQuestionnaire = questionnaireService.updateQuestionnaire(createQuestionnaireDto)
        val inDBQuestionnaire = questionnaireService.findQuestionnaireWithQuestions(savedQuestionnaire.id)
        inDBQuestionnaire shouldNotBe null
        savedQuestionnaire.title shouldBe createQuestionnaireDto.title
        createQuestionnaireDto.title shouldBe inDBQuestionnaire.title
        inDBQuestionnaire.questions.size shouldBe 0
    }

    @Test
    fun `QYoga can save questionnaire with questions without image and answers`() {
        val question1 = CreateQuestionDto()
        question1.title = ""
        question1.questionType = QuestionType.TEXT
        val question2 = CreateQuestionDto()
        question2.title = ""
        question2.questionType = QuestionType.SEVERAL
        val question3 = CreateQuestionDto()
        question3.title = ""
        question3.questionType = QuestionType.TEXT
        val questionnaireWithCreateQuestionDto = CreateQuestionnaireDto(
            id = 0,
            title = "create questionnaire test",
            questions = mutableListOf(
                question1,
                question2,
                question3
            )
        )
        val savedQuestionnaire = questionnaireService.updateQuestionnaire(questionnaireWithCreateQuestionDto)
        val inDBQuestionnaire = questionnaireService.findQuestionnaireWithQuestions(savedQuestionnaire.id)
        inDBQuestionnaire shouldNotBe null
        savedQuestionnaire.title shouldBe inDBQuestionnaire.title
        inDBQuestionnaire.questions.size shouldBe 3
        for (question in inDBQuestionnaire.questions) {
            question.answers.size shouldBe 0
        }
    }

    @Test
    fun `QYoga can save questionnaire with questions and answers without image`() {
        val questionnaireWithCreateQuestionDto = CreateQuestionnaireDto(
            id = 0,
            title = "create questionnaire test"
        )
        val savedQuestionnaire = questionnaireService.updateQuestionnaire(questionnaireWithCreateQuestionDto)
        questionService.createQuestion(savedQuestionnaire.id)
        val inDBQuestionnaire = questionnaireService.findQuestionnaireWithQuestions(savedQuestionnaire.id)
        savedQuestionnaire.title shouldBe inDBQuestionnaire.title
        inDBQuestionnaire.questions.size shouldBe 1
        inDBQuestionnaire.questions[0].answers.size shouldBe 1
    }

    @Test
    fun `QYoga can create empty questionnaire by default`() {
        val questionnaireId = questionnaireService.createQuestionnaire()
        val questionnaire = questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        questionnaire.id shouldBe questionnaireId
        questionnaire.title shouldBe ""
        questionnaire.questions.size shouldBe 0
    }

    @Test
    fun `QYoga can QuestionnaireDto`() {
        val savedQuestionnaire = questionnaireService.updateQuestionnaire(
            QuestionnaireDto(id = 0, title = "test")
        )
        val inDbQuestionnaire = questionnaireService.findQuestionnaireWithQuestions(savedQuestionnaire.id)
        savedQuestionnaire.id shouldBe inDbQuestionnaire.id
        savedQuestionnaire.title shouldBe inDbQuestionnaire.title
        inDbQuestionnaire.questions.size shouldBe 0
    }
}
