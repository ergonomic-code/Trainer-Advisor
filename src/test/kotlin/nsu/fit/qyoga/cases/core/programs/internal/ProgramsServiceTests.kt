package nsu.fit.qyoga.cases.core.programs.internal

import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.programs.ProgramsTestConfig
import nsu.fit.qyoga.core.programs.api.ProgramsService
import nsu.fit.qyoga.core.programs.api.dtos.ProgramSearchDto
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
    classes = [ProgramsTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
class ProgramsServiceTests(
    @Autowired private val programsService: ProgramsService,
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/exercises/exercises-init-script.sql" to "dataSource",
            "/db/exercises/exercises-insert-data-script.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can retrieve programs without filters`() {
        // Given
        val searchDto = ProgramSearchDto()

        // When
        val programs = programsService.getPrograms(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        programs.content.size shouldBe 5
        programs.totalElements shouldBe 5
        programs.content.map { it.id.toInt() }.sorted() shouldBe listOf(1, 2, 3, 4, 5)
    }

    @Test
    fun `QYoga can retrieve programs with filters`() {
        // Given
        val searchDto = ProgramSearchDto(title = "Людмила")

        // When
        val programs = programsService.getPrograms(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        programs.content.size shouldBe 1
        programs.totalElements shouldBe 1
        programs.content.map { it.title }[0].startsWith("Программа")
        programs.content.map { it.id } shouldBe listOf(1)
    }

    @Test
    fun `QYoga shouldn't retrieve programs with invalid filter`() {
        // Given
        val searchDto = ProgramSearchDto(title = "====")

        // When
        val programs = programsService.getPrograms(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        programs.content.size shouldBe 0
        programs.totalElements shouldBe 0
    }
}