package pro.qyoga.tests.cases.app.therapist.clients.card

import io.kotest.inspectors.forAny
import io.kotest.inspectors.forNone
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.clients.card.EditClientPage
import pro.qyoga.tests.fixture.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class EditClientCardPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Client card page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()

        // When
        val document = therapist.clients.getEditClientCardPage(client.id)

        // Then
        document shouldBe EditClientPage.pageFor(client)
    }

    @Test
    fun `Client editing should be persistent`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        val newClientCardDto = ClientsObjectMother.createClientCardDto()
        val client = backgrounds.clients.createClients(listOf(newClientCardDto), THE_THERAPIST_ID).first()

        val editedClientCardDto = ClientsObjectMother.createClientCardDto()

        // When
        therapist.clients.editClient(client.id, editedClientCardDto)

        // Then
        val clients = backgrounds.clients.getAllClients().content
        clients.forNone { it shouldMatch newClientCardDto }
        clients.forAny { it shouldMatch editedClientCardDto }
    }

    @Test
    fun `System should accept edit client forms containing only required fields`() {
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