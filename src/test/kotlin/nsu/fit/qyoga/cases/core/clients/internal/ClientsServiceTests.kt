package nsu.fit.qyoga.cases.core.clients.internal

import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import nsu.fit.qyoga.cases.core.clients.ClientsTestConfig
import nsu.fit.qyoga.core.clients.api.ClientService
import nsu.fit.qyoga.core.clients.api.Dto.ClientSearchDto
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

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
            "/db/clients-init-script.sql" to "dataSource",
            "/db/insert-clients.sql" to "dataSource",
        )
    }

    @Test
    fun `QYoga can retrieve clients without filters`() {
        // Given
        val searchDto = ClientSearchDto()

        // When
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        clients.content.size shouldBe 3
        clients.totalElements shouldBe 3
    }

    @Test
    fun `QYoga can retrieve clients with filters`() {
        // Given
        val searchDto = ClientSearchDto(lastName = "Иванов")

        // When
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        clients.content.size shouldBe 1
        clients.totalElements shouldBe 1
        clients.content.forAll { it.lastName shouldContain ("Иванов") }
    }

    @Test
    fun `QYoga can retrieve clients by appointment date`() {
        // Given
        val searchDto = ClientSearchDto(appointmentDate = LocalDate.of(2023, 3, 18))

        // When
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        clients.content.size shouldBe 2
        clients.totalElements shouldBe 2
    }

    @Test
    fun `QYoga shouldn't retrieve clients with invalid filter`() {
        // Given
        val searchDto = ClientSearchDto(firstName = "---")

        // When
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        clients.content.size shouldBe 0
        clients.totalElements shouldBe 0
    }

    @Test
    fun `QYoga should delete clients with delete-button`() {
        // Given
        val searchDto = ClientSearchDto()

        // When
        clientService.deleteClient(1)
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(0, 10)
        )
        // Then
        clients.content.size shouldBe 2
        clients.totalElements shouldBe 2
    }

    @Test
    fun `QYoga should redirect to clients-list when delete can't client`() {
        // Given
        val searchDto = ClientSearchDto()
        clientService.deleteClient(100)

        // When
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(0, 10)
        )
        // Then
        clients.content.size shouldBe 3
        clients.totalElements shouldBe 3
    }

    @Test
    fun `QYoga should delete clients with delete-button`() {
        // Given
        val searchDto = ClientListSearchDto()
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(0, 10)
        )
        // When

        clientService.deleteClient(4)

        // Then
        clients.content.size shouldBe 2
        clients.totalElements shouldBe 2
    }
}
