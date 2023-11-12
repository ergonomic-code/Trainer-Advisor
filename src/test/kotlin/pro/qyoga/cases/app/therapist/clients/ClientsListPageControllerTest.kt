package pro.qyoga.cases.app.therapist.clients

import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.core.clients.api.ClientDto
import pro.qyoga.infra.test_config.ClientsTestConfig


class ClientsListPageControllerTest {

    @Test
    fun `Clients table should be sorted by last name`() {
        // Given
        val clientsCount = 10
        val clientListPageController = ClientsTestConfig.clientListPageController

        ClientsTestConfig.clientsBackgrounds.createClients(clientsCount)

        val model = ExtendedModelMap()

        // When
        clientListPageController.getClients(PageRequest.ofSize(clientsCount), model)

        // Then
        @Suppress("UNCHECKED_CAST")
        val clients = (model["clients"] as Page<ClientDto>).content
        clients shouldHaveSize clientsCount
        clients shouldBeSortedWith Comparator.comparing { it.lastName.lowercase() }
    }

}