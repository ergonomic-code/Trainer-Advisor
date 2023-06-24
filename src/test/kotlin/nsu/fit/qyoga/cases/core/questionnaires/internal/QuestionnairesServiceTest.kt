package nsu.fit.qyoga.cases.core.questionnaires.internal

import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import nsu.fit.qyoga.cases.core.questionnaires.QuestionnairesTestConfig
import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.model.Image
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
class QuestionnairesServiceTest(
    @Autowired private val questionnaireService: QuestionnaireService,
    @Autowired private val imageService: ImageService
) : QYogaModuleBaseTest() {
    private val file = File("src/test/resources/images/testImage.png")

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
        val questionnaireToSave = CreateQuestionnaireDto(
            id = 0,
            title = "create questionnaire test"
        )
        val savedQuestionnaireId = questionnaireService.saveQuestionnaire(questionnaireToSave)
        val savedQuestionnaire = questionnaireService.findQuestionnaireWithQuestions(savedQuestionnaireId)
        savedQuestionnaire shouldNotBe null
        savedQuestionnaire.title shouldBe questionnaireToSave.title
        savedQuestionnaire.question.size shouldBe 0
        savedQuestionnaire.decoding.size shouldBe 0
    }

    @Test
    fun `QYoga can save questionnaire with questions without image and answers`() {
        val questionnaireToSave = CreateQuestionnaireDto(
            id = 0,
            title = "create questionnaire test",
            question = mutableListOf(
                CreateQuestionDto(
                    title = "title1",
                    questionType = QuestionType.TEXT
                ),
                CreateQuestionDto(
                    title = "title2",
                    questionType = QuestionType.SEVERAL
                ),
                CreateQuestionDto(
                    title = "title3",
                    questionType = QuestionType.TEXT
                )
            )
        )
        val savedQuestionnaireId = questionnaireService.saveQuestionnaire(questionnaireToSave)
        val savedQuestionnaire = questionnaireService.findQuestionnaireWithQuestions(savedQuestionnaireId)
        savedQuestionnaire shouldNotBe null
        savedQuestionnaire.title shouldBe questionnaireToSave.title
        savedQuestionnaire.question.size shouldBe 3
        for (question in questionnaireToSave.question) {
            savedQuestionnaire.question.filter {
                it.title == question.title && it.questionType == question.questionType
            }.size shouldBe 1
        }
    }

    @Test
    fun `QYoga can save questionnaire with questions and answers without image`() {
        val questionnaireToSave = CreateQuestionnaireDto(
            id = 0,
            title = "create questionnaire test",
            question = mutableListOf(
                CreateQuestionDto(
                    id = 0,
                    title = null,
                    questionType = QuestionType.TEXT,
                    imageId = null,
                    answers = mutableListOf(
                        CreateAnswerDto(
                            id = 0,
                            title = "test save",
                            AnswerBoundsDto(
                                lowerBound = null,
                                lowerBoundText = null,
                                upperBound = null,
                                upperBoundText = null
                            ),
                            score = null,
                            imageId = null
                        )
                    )
                )
            )
        )
        val savedQuestionnaireId = questionnaireService.saveQuestionnaire(questionnaireToSave)
        val savedQuestionnaire = questionnaireService.findQuestionnaireWithQuestions(savedQuestionnaireId)
        savedQuestionnaire shouldNotBe null
        savedQuestionnaire.title shouldBe questionnaireToSave.title
        savedQuestionnaire.question.size shouldBe 1
        savedQuestionnaire.question[0].answers.size shouldBe 1
        savedQuestionnaire.question[0].answers[0].title shouldBe questionnaireToSave.question[0].answers[0].title
    }

    @Test
    fun `QYoga can save questionnaire with questions, answers and image`() {
        val multipartFile = getMultipartFileFromInputStream(FileInputStream(file))
        val image = multipartFileToImage(multipartFile)
        val imageId = imageService.uploadImage(image)
        val questionnaireToSave = CreateQuestionnaireDto(
            id = 0,
            title = "create questionnaire test",
            question = mutableListOf(
                CreateQuestionDto(
                    id = 0,
                    title = null,
                    questionType = QuestionType.TEXT,
                    imageId = imageId,
                    answers = mutableListOf(
                        CreateAnswerDto(
                            id = 0,
                            title = "test save",
                            AnswerBoundsDto(
                                lowerBound = null,
                                lowerBoundText = null,
                                upperBound = null,
                                upperBoundText = null
                            ),
                            score = null,
                            imageId = imageId
                        )
                    )
                )
            )
        )
        val savedQuestionnaireId = questionnaireService.saveQuestionnaire(questionnaireToSave)
        val savedQuestionnaire = questionnaireService.findQuestionnaireWithQuestions(savedQuestionnaireId)
        savedQuestionnaire shouldNotBe null
        savedQuestionnaire.question[0].imageId shouldBe imageId
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
