package pro.qyoga.tests.cases.app.therapist.clients

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class ClientPageControllerTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `system should accept create client forms containing only required fields`() {
        // Given
        val minimalClient = ClientsObjectMother.createClientCardDtoMinimal()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.createClient(minimalClient)

        // Then
        val clients = backgrounds.clients.getAllClients().content
        clients.forAny { it shouldMatch minimalClient }
    }

    @Test
    fun `system should accept edit client forms containing only required fields`() {
        // Given
        val minimalClient = ClientsObjectMother.createClientCardDtoMinimal()
        val editedMinimalClient = ClientsObjectMother.createClientCardDtoMinimal()
        val minimalClientId = backgrounds.clients.createClients(listOf(minimalClient), THE_THERAPIST_ID).single().id
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.editClient(minimalClientId, editedMinimalClient)

        // Then
        val clients = backgrounds.clients.getAllClients().content
        clients.forAny { it shouldMatch editedMinimalClient }
    }

}