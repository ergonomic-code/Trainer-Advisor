package pro.qyoga.cases.app.therapist.clients

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldBe
import pro.qyoga.assertions.shouldHave
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.core.clients.api.ClientSearchDto
import pro.qyoga.fixture.clients.ClientDtoObjectMother
import pro.qyoga.fixture.clients.ClientDtoObjectMother.createClientDto
import pro.qyoga.infra.web.QYogaAppBaseTest
import java.time.LocalDate


class ClientsListPageTest : QYogaAppBaseTest() {

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
        val clients = ClientDtoObjectMother.createClientDtos(pageSize + 1)
        val firstPage = clients.sortedBy { it.lastName.lowercase() }
            .take(pageSize)
        backgrounds.clients.createClients(clients)

        // When
        val document = therapist.clients.getClientsListPage()

        // Then
        document shouldBe ClientsListPage
        ClientsListPage.clientRows(document) shouldHaveSize pageSize
        firstPage.forAll {
            document shouldHave ClientsListPage.clientRow(it)
        }
    }

    @Test
    fun `When user submits search from, response should contain only rows matching query`() {
        // Given
        val firstName = "Иван"
        val lastName = "Иванов"
        val patronymic = "Иванович"
        val birthDate = LocalDate.of(2000, 1, 12)
        val phonePart = "923-111"

        val fullMatch1 = createClientDto(firstName, lastName, patronymic, birthDate, "+7-$phonePart-22-44")
        val fullMatch2 = createClientDto(firstName, lastName + "ский", patronymic, birthDate, "+7-$phonePart-22-33")
        val nonMatchByLastName = createClientDto(firstName, lastName = lastName.reversed())
        val nonMatchByPatronymic = createClientDto(firstName, lastName, patronymic = patronymic.reversed())
        val nonMatchByPhone = createClientDto(firstName, lastName, patronymic, birthDate, phone = phonePart.reversed())
        backgrounds.clients.createClients(
            listOf(
                fullMatch1,
                fullMatch2,
                nonMatchByLastName,
                nonMatchByPatronymic,
                nonMatchByPhone
            )
        )

        val searchForm = ClientSearchDto(firstName, lastName, patronymic, phonePart)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.searchClients(searchForm)

        // Then
        ClientsListPage.clientRows(document) shouldHaveSize 2
        document shouldHave ClientsListPage.clientRow(fullMatch1)
        document shouldHave ClientsListPage.clientRow(fullMatch2)
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