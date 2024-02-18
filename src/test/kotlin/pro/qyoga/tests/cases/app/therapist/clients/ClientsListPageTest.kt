package pro.qyoga.tests.cases.app.therapist.clients

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.cards.api.ClientSearchDto
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.LocalDate


class ClientsListPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Clients list page should be correctly rendered when there are no clients`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.getClientsListPage()

        // Then
        document shouldBe ClientsListPage
        ClientsListPage.clientRows(document) shouldHaveSize 0
    }

    @Test
    fun `Clients list page should render 10 rows when enough clients exists`() {
        // Given
        val pageSize = 10
        val therapist = TherapistClient.loginAsTheTherapist()
        val clients = ClientsObjectMother.createClientCardDtos(pageSize + 1)
        val firstPage = clients.sortedBy { it.lastName.lowercase() }.take(pageSize)
        backgrounds.clients.createClients(clients)

        // When
        val document = therapist.clients.getClientsListPage()

        // Then
        document shouldBe ClientsListPage
        ClientsListPage.clientRows(document) shouldHaveSize pageSize
        firstPage.forAll {
            document shouldHave ClientsListPage.clientRow(Client(THE_THERAPIST_ID, it))
        }
    }

    @Test
    fun `When user submits search from, response should contain only rows matching query`() {
        // Given
        val firstName = "Иван"
        val lastName = "Иванов"
        val birthDate = LocalDate.of(2000, 1, 12)
        val phonePart = "923-111"

        val fullMatch1 =
            createClientCardDto(firstName, lastName, birthDate = birthDate, phone = "+7-$phonePart-22-44")
        val fullMatch2 =
            createClientCardDto(firstName, lastName + "ский", birthDate = birthDate, phone = "+7-$phonePart-22-33")
        val nonMatchByLastName =
            createClientCardDto(firstName, lastName = lastName.reversed())
        val nonMatchByPhone =
            createClientCardDto(firstName, lastName, birthDate = birthDate, phone = phonePart.reversed())

        backgrounds.clients.createClients(
            listOf(
                fullMatch1,
                fullMatch2,
                nonMatchByLastName,
                nonMatchByPhone
            )
        )

        val searchForm = ClientSearchDto(firstName, lastName, phonePart)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.searchClients(searchForm)

        // Then
        ClientsListPage.clientRows(document) shouldHaveSize 2
        document shouldHave ClientsListPage.clientRow(Client(THE_THERAPIST_ID, fullMatch1))
        document shouldHave ClientsListPage.clientRow(Client(THE_THERAPIST_ID, fullMatch2))
    }

    @Test
    fun `After click on deletion button client should disappear from client table`() {
        // Given
        backgrounds.clients.createClients(1)
        val therapist = TherapistClient.loginAsTheTherapist()
        val document = therapist.clients.getClientsListPage()
        val clientId = ClientsListPage.clientId(document, 0)

        // When
        therapist.clients.deleteClient(clientId)

        // Then
        ClientsListPage.clientRows(therapist.clients.getClientsListPage()) shouldHaveSize 0
    }

}