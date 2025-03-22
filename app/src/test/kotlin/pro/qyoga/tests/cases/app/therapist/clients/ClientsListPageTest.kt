package pro.qyoga.tests.cases.app.therapist.clients

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.pages.therapist.clients.ClientsListPagination
import java.time.LocalDate


@DisplayName("Страница списка клиентов")
class ClientsListPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `должна рендерится корректно с пустым списком`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.getClientsListPage()

        // Then
        document shouldBe ClientsListPage
        ClientsListPage.clientRows(document) shouldHaveSize 0
    }

    @Test
    fun `должна отбражать 10 клиентов, когда в БД более 10 записей`() {
        // Given
        val pageSize = 10
        val therapist = TherapistClient.loginAsTheTherapist()
        val clients = ClientsObjectMother.createClientCardDtos(pageSize + 1)
        val firstPage = clients.reversed().take(pageSize)
        backgrounds.clients.createClients(clients)

        // When
        val document = therapist.clients.getClientsListPage()

        // Then
        document shouldBe ClientsListPage
        ClientsListPage.clientRows(document) shouldHaveSize pageSize
        firstPage.forAll {
            document shouldHave ClientsListPage.clientRow(createClient(THE_THERAPIST_ID, it))
        }
        document shouldHaveComponent ClientsListPagination(pages = 2, currentPage = 1)
    }

    @Test
    fun `при фильтрации должна возвращать только строки соответствуюище фильтру`() {
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
            createClientCardDto(firstName, lastName, birthDate = birthDate, phone = "+7-${phonePart.reversed()}-22-44")

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
        document shouldHave ClientsListPage.clientRow(createClient(THE_THERAPIST_ID, fullMatch1))
        document shouldHave ClientsListPage.clientRow(createClient(THE_THERAPIST_ID, fullMatch2))
    }

    @Test
    fun `должна не содержать клиента после его удаления`() {
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

    @Test
    fun `должна корректно рендерить фрагмент второй страницы списка`() {
        // Given
        val page = 2
        val pageSize = 10
        val therapist = TherapistClient.loginAsTheTherapist()
        val clients = ClientsObjectMother.createClientCardDtos(pageSize * 2 + 1)
        val secondPage = clients.reversed().drop(pageSize).take(pageSize)
        backgrounds.clients.createClients(clients)

        // When
        val document = therapist.clients.searchClients(page = page)

        // Then
        ClientsListPage.clientRows(document) shouldHaveSize pageSize
        secondPage.forAll {
            document shouldHave ClientsListPage.clientRow(createClient(THE_THERAPIST_ID, it))
        }
        document shouldHaveComponent ClientsListPagination(pages = 3, currentPage = page)
    }

}