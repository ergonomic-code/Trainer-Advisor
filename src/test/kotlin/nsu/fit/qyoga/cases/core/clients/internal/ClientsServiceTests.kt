package nsu.fit.qyoga.cases.core.clients.internal

import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.clients.ClientsTestConfig
import nsu.fit.qyoga.core.clients.api.ClientService
import nsu.fit.qyoga.core.clients.api.Dto.ClientListSearchDto
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

class ClientsServiceTests {
    @ContextConfiguration(
        classes = [ClientsTestConfig::class],
        initializers = [TestContainerDbContextInitializer::class]
    )
    @SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
    )
    @ActiveProfiles("test")
    class ClientsServiceTests(
        @Autowired private val clientService: ClientService
    ) : QYogaModuleBaseTest() {

        @BeforeEach
        fun setupDb() {
            dbInitializer.executeScripts(
                "/db/clients" to "dataSource",
                "/db/insert_clients" to "dataSource"
            )
        }

        @Test
        fun `QYoga can retrieve clients without filters`() {
            // Given
            val searchDto = ClientListSearchDto()

            // When
            val clients = clientService.getClients(
                searchDto,
                PageRequest.of(0, 10)
            )

            // Then
            clients.content.size shouldBe 1
            clients.totalElements shouldBe 1
            clients.content.map { it.id.toInt() } shouldBe listOf(1)
        }

        @Test
        fun `QYoga can retrieve clients with filters`() {
            // Given
            val searchDto = ClientListSearchDto(surname = "Brown")

            // When
            val clients = clientService.getClients(
                searchDto,
                PageRequest.of(0, 10)
            )

            // Then
            clients.content.size shouldBe 1
            clients.totalElements shouldBe 1
            clients.content.map { it.surname }[0].startsWith("Brown")
            clients.content.map { it.id } shouldBe listOf(1)
        }

        @Test
        fun `QYoga shouldn't retrieve clients with invalid filter`() {
            // Given
            val searchDto = ClientListSearchDto(name = "---")

            // When
            val clients = clientService.getClients(
                searchDto,
                PageRequest.of(0, 10)
            )

            // Then
            clients.content.size shouldBe 0
            clients.totalElements shouldBe 0
        }
    }
}
