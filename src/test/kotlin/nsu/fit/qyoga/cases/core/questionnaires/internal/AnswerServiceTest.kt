package nsu.fit.qyoga.cases.core.questionnaires.internal

import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.errors.AnswerException
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
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
class AnswerServiceTest(
    @Autowired private val answerService: AnswerService
) : QYogaModuleBaseTest() {
    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-question.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can create empty answer by default`() {
        val answer = answerService.createAnswer(1)
        answer.id shouldBeGreaterThan -1
        answer.title shouldBe ""
        answer.score shouldBe null
        answer.questionId shouldBe 1
        answer.bounds.lowerBound shouldBe null
        answer.bounds.lowerBoundText shouldBe null
        answer.bounds.upperBound shouldBe null
        answer.bounds.upperBoundText shouldBe null
    }

    @Test
    fun `QYoga can find answer by id`() {
        val createdAnswer = answerService.createAnswer(1)
        val answer = answerService.findAnswer(createdAnswer.id)
        answer.id shouldBe createdAnswer.id
        answer.title shouldBe ""
        answer.score shouldBe null
        answer.questionId shouldBe 1
        answer.bounds.lowerBound shouldBe null
        answer.bounds.lowerBoundText shouldBe null
        answer.bounds.upperBound shouldBe null
        answer.bounds.upperBoundText shouldBe null
    }

    @Test
    fun `QYoga can delete answers by question id`() {
        val listAnswers: MutableList<AnswerDto> = mutableListOf()
        repeat(3) {
            listAnswers.add(answerService.createAnswer(1))
        }
        listAnswers.size shouldBe 3
        val deletedAnswers = answerService.deleteAllByQuestionId(listAnswers[0].id)
        listAnswers.size shouldBe deletedAnswers.size
        var thrown: AnswerException
        for (answer in deletedAnswers) {
            thrown = Assertions.assertThrows(
                AnswerException::class.java
            ) { answerService.findAnswer(listAnswers[0].id) }
            Assertions.assertEquals("Выбранный ответ не найден", thrown.message)
        }
    }

    @Test
    fun `QYoga can update or save answer`() {
        val answer = answerService.createAnswer(id = 1)
        answer.title = "test1"
        answer.score = 0
        val changedAnswer = answerService.updateAnswer(answer)
        changedAnswer.id shouldBe answer.id
        changedAnswer.title shouldBe "test1"
        changedAnswer.score shouldBe 0
        changedAnswer.questionId shouldBe 1
        changedAnswer.bounds.lowerBound shouldBe null
        changedAnswer.bounds.lowerBoundText shouldBe null
        changedAnswer.bounds.upperBound shouldBe null
        changedAnswer.bounds.upperBoundText shouldBe null
    }

    @Test
    fun `QYoga can delete answers by id`() {
        val answer = answerService.createAnswer(1)
        answerService.deleteAnswerById(answer.id)
        val thrown: AnswerException = Assertions.assertThrows(
            AnswerException::class.java
        ) { answerService.findAnswer(answer.id) }
        Assertions.assertEquals("Выбранный ответ не найден", thrown.message)
    }
}
