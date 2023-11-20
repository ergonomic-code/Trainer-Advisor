package pro.qyoga.cases.app.therapist.clients

import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.clients.ClientListPageController
import pro.qyoga.core.clients.api.ClientDto
import pro.qyoga.infra.web.QYogaAppBaseTest


class ClientsListPageControllerTest : QYogaAppBaseTest() {

    private val clientListPageController = getBean<ClientListPageController>()

    @Test
    fun `Clients table should be sorted by last name`() {
        // Given
        val clientsCount = 10

        backgrounds.clients.createClients(clientsCount)

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