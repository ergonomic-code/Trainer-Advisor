package nsu.fit.qyoga.cases.core.questionnaires.internal

import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionException
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.Assertions
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
class QuestionServiceTest(
    @Autowired private val questionService: QuestionService
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can create question by default`() {
        val question = questionService.createQuestion(1)
        question.answers.size shouldBe 0
        question.questionType shouldBe QuestionType.SINGLE
        question.title shouldBe ""
        question.imageId shouldBe null
    }

    @Test
    fun `QYoga can create question from QuestionWithAnswersDto and find them`() {
        val questionId = questionService.updateQuestion(
            QuestionWithAnswersDto(
                id = 0,
                title = "test",
                questionType = QuestionType.RANGE,
                imageId = null,
                questionnaireId = 1
            )
        )
        val question = questionService.findQuestionWithAnswers(questionId)
        question.questionnaireId shouldBe 1
        question.questionType shouldBe QuestionType.RANGE
        question.imageId shouldBe null
        question.title shouldBe "test"
        question.answers.size shouldBe 1
    }

    @Test
    fun `QYoga find question by id`() {
        val questionId = questionService.createQuestion(1).id
        val question = questionService.findQuestion(questionId)
        question.questionnaireId shouldBe 1
        question.questionType shouldBe QuestionType.SINGLE
        question.title shouldBe ""
        question.imageId shouldBe null
    }

    @Test
    fun `QYoga can delete question by id`() {
        val questionId = questionService.createQuestion(1).id
        questionService.deleteQuestion(questionId)
        val thrown1: QuestionException = Assertions.assertThrows(
            QuestionException::class.java
        ) { questionService.findQuestion(questionId) }
        Assertions.assertEquals("Выбранный вопрос не найден", thrown1.message)
    }
}
