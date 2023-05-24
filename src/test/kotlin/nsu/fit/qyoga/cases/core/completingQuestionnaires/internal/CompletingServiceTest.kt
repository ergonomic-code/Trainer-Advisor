package nsu.fit.qyoga.cases.core.completingQuestionnaires.internal

import io.kotest.matchers.ints.shouldBeLessThan
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
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    classes = [CompletingQuestionnairesTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
class CompletingServiceTest(
    @Autowired private val completingService: CompletingService,
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/completing/completing-questionnaires-init-script.sql" to "dataSource",
            "/db/migration/common/V23051304__insert_questionnaires_data.sql" to "dataSource",
            "/db/completing/insert_completing_data.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can retrieve questionnaires without title`() {
        val completing = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(),
            PageRequest.of(0, 10, Sort.by("date").ascending())
        )
        completing.content.size shouldBe 10
        completing.content.map { it.id.toInt() } shouldBe listOf(1, 2, 3, 4, 5, 6, 7, 8, 10, 12)
    }

    @Test
    fun `QYoga can retrieve questionnaires with different type of sort`() {
        val completingASK = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(),
            PageRequest.of(0, 10, Sort.by("date").ascending())
        )
        val completingDESK = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(),
            PageRequest.of(0, 10, Sort.by("date").descending())
        )
        completingASK.content.size shouldBe 10
        completingASK.content.map { it.id.toInt() } shouldBe listOf(1, 2, 3, 4, 5, 6, 7, 8, 10, 12)
        completingDESK.content.size shouldBe 10
        completingDESK.content.map { it.id.toInt() } shouldBe listOf(13, 12, 10, 8, 7, 6, 5, 4, 3, 2)
    }

    @Test
    fun `QYoga can retrieve questionnaires page by page`() {
        val completingPage2 = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(),
            PageRequest.of(1, 10, Sort.by("date").ascending())
        )
        completingPage2.content.size shouldBeLessThan 10
        completingPage2.content[0].id shouldBe 13
        completingPage2.content[0].numericResult shouldBe 10
        completingPage2.content[0].textResult shouldBe "large_text_result_123456789011121314151617181920"
    }

    @Test
    fun `QYoga can retrieve questionnaires by title`() {
        val completing = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(title = "test"),
            PageRequest.of(0, 10, Sort.by("date").ascending())
        )
        completing.content.size shouldBe 5
        val regex = """.*test.*""".toRegex()
        for (c in completing.content) {
            regex.containsMatchIn(c.questionnaire.title) shouldBe true
        }
    }

    @Test
    fun `QYoga can retrieve questionnaires by client first name`() {
        val completing = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(clientName = "first_name"),
            PageRequest.of(0, 10, Sort.by("date").ascending())
        )
        completing.content.size shouldBe 10
        val regex = """.*first_name.*""".toRegex()
        for (c in completing.content) {
            regex.containsMatchIn(c.client.firstName) shouldBe true
        }
    }

    @Test
    fun `QYoga can retrieve questionnaires by client last name`() {
        val completing = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(clientName = "last_name"),
            PageRequest.of(0, 10, Sort.by("date").ascending())
        )
        val regex = """.*last_name.*""".toRegex()
        for (c in completing.content) {
            regex.containsMatchIn(c.client.lastName) shouldBe true
        }
    }

    @Test
    fun `QYoga can retrieve questionnaires by client patronymic`() {
        val completing = completingService.findCompletingByTherapistId(
            1,
            CompletingSearchDto(clientName = "patronymic"),
            PageRequest.of(0, 10, Sort.by("date").ascending())
        )
        completing.content.size shouldBe 10
        val regex = """.*patronymic.*""".toRegex()
        for (c in completing.content) {
            regex.containsMatchIn(c.client.patronymic) shouldBe true
        }
    }
}
