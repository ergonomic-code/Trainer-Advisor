package pro.qyoga.cases.app.therapist.clients

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldBe
import pro.qyoga.assertions.shouldMatch
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.therapist.clients.CreateClientPage
import pro.qyoga.fixture.clients.ClientsObjectMother
import pro.qyoga.infra.web.QYogaAppBaseTest

class CreateClientTest : QYogaAppBaseTest() {

    @Test
    fun `Create client page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.getCreateClientPage()

        // Then
        document shouldBe CreateClientPage
    }

    @Test
    fun `After creating a client, it should appear in the client table`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientRequest = ClientsObjectMother.createClientRequest()

        // When
        therapist.clients.createClient(newClientRequest)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.content.forAny { it shouldMatch newClientRequest }
    }

}