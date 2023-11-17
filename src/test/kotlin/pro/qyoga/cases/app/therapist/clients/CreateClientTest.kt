package pro.qyoga.cases.app.therapist.clients

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldHave
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.core.clients.api.CreateClientRequest
import pro.qyoga.fixture.clients.ClientDtoObjectMother
import pro.qyoga.infra.web.QYogaAppBaseTest
import java.time.LocalDate

class CreateClientTest : QYogaAppBaseTest() {

    @Test
    fun `After creating a client, it should appear in the client table`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val initialDocument = therapist.clients.getClientsListPage()
        val initialClientCount = ClientsListPage.clientRows(initialDocument).size

        val newClientRequest = CreateClientRequest(
            firstName = "John",
            lastName = "Doe",
            middleName = "Smith",
            birthDate = LocalDate.of(1990, 5, 15),
            phoneNumber = "1234567890",
            email = "john.doe@example.com",
            areaOfResidence = "City",
            distributionSource = "Referral",
            complains = "None"
        )

        // When
        therapist.clients.createClient(newClientRequest)

        // Then
        val updatedDocument = therapist.clients.getClientsListPage()
        val updatedClientCount = ClientsListPage.clientRows(updatedDocument).size

        updatedClientCount shouldBe (initialClientCount + 1)

        // Additional assertions to verify details of the newly created client
        val newClient = ClientDtoObjectMother.createClientDto(
            newClientRequest.firstName,
            newClientRequest.lastName,
            newClientRequest.middleName,
            newClientRequest.birthDate,
            newClientRequest.phoneNumber,
            newClientRequest.email!!,
            newClientRequest.areaOfResidence!!,
            newClientRequest.complains,
        )

        updatedDocument shouldHave ClientsListPage.clientRow(newClient)
    }

    //todo more tests
}