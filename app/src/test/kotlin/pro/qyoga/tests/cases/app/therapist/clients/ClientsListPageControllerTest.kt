package pro.qyoga.tests.cases.app.therapist.clients

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.clients.ClientsListPageController
import pro.qyoga.core.clients.cards.api.ClientSearchDto
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class ClientsListPageControllerTest : QYogaAppIntegrationBaseTest() {

    private val clientsListPageController = getBean<ClientsListPageController>()

    @Test
    fun `Clients table should be sorted by last name`() {
        // Given
        val clientsCount = 10

        backgrounds.clients.createClients(clientsCount)

        val model = ExtendedModelMap()

        // When
        clientsListPageController.getClients(
            theTherapistUserDetails,
            PageRequest.ofSize(clientsCount),
            model
        )

        // Then
        val clients = ClientsListPageController.getClients(model)
        clients shouldHaveSize clientsCount
        clients shouldBeSortedWith Comparator.comparing { it.lastName.lowercase() }
    }

    @Test
    fun `Clients list page should contain only therapist's own clients`() {
        // Given
        val ownClientsCount = 5
        val alienClientsCount = 5

        val anotherTherapist = backgrounds.users.registerNewTherapist()

        backgrounds.clients.createClients(ownClientsCount, THE_THERAPIST_ID)
        backgrounds.clients.createClients(alienClientsCount, anotherTherapist.id)

        val model = ExtendedModelMap()

        // When
        clientsListPageController.getClients(
            theTherapistUserDetails,
            PageRequest.ofSize(Int.MAX_VALUE),
            model
        )
        val clients = ClientsListPageController.getClients(model)

        // Then
        clients.content shouldHaveSize ownClientsCount
        clients.content.forAll { it.therapistId shouldBe THE_THERAPIST_REF }
    }

    @Test
    fun `Clients search page should contain only therapist's own clients`() {
        // Given
        val ownClientsCount = 5
        val alienClientsCount = 5

        val anotherTherapist = backgrounds.users.registerNewTherapist()

        backgrounds.clients.createClients(ownClientsCount, THE_THERAPIST_ID)
        backgrounds.clients.createClients(alienClientsCount, anotherTherapist.id)

        val model = ExtendedModelMap()

        // When
        clientsListPageController.getClientsFiltered(
            theTherapistUserDetails,
            ClientSearchDto.ALL,
            PageRequest.ofSize(Int.MAX_VALUE),
            model
        )
        val clients = ClientsListPageController.getClients(model)

        // Then
        clients.content shouldHaveSize ownClientsCount
        clients.content.forAll { it.therapistId shouldBe THE_THERAPIST_REF }
    }

}