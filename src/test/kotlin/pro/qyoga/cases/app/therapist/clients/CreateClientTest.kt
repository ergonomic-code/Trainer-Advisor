package pro.qyoga.cases.app.therapist.clients

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldHave
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.fixture.clients.ClientDtoObjectMother
import pro.qyoga.infra.web.QYogaAppBaseTest

class CreateClientTest : QYogaAppBaseTest() {

    @Test
    fun `After creating a client, it should appear in the client table`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val initialDocument = therapist.clients.getClientsListPage()
        val initialClientCount = ClientsListPage.clientRows(initialDocument).size

        val newClientRequest = ClientDtoObjectMother.createClientRequest()

        // When
        therapist.clients.createClient(newClientRequest)

        // Then
        val updatedDocument = therapist.clients.getClientsListPage()
        val updatedClientCount = ClientsListPage.clientRows(updatedDocument).size

        updatedClientCount shouldBe (initialClientCount + 1)

        val newClient = ClientDtoObjectMother.createClientDto(newClientRequest)

        updatedDocument shouldHave ClientsListPage.clientRow(newClient)
    }

    //todo more tests
}