package pro.qyoga.cases.app.therapist.clients

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.fixture.clients.ClientDtoObjectMother
import pro.qyoga.infra.test_config.ClientsTestConfig
import pro.qyoga.infra.web.QYogaAppBaseTest

class CreateClientTest : QYogaAppBaseTest() {

    @Test
    fun `After creating a client, it should appear in the client table`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientRequest = ClientDtoObjectMother.createClientRequest()

        // When
        therapist.clients.createClient(newClientRequest)

        // Then
        val newClient = ClientDtoObjectMother.createClientDto(newClientRequest)
        ClientsTestConfig.clientsBackgrounds.exists(newClient).shouldBe(true)
    }

    //todo more tests
}