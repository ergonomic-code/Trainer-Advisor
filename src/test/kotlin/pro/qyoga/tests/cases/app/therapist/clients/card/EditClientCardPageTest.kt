package pro.qyoga.tests.cases.app.therapist.clients.card

import io.kotest.inspectors.forAny
import io.kotest.inspectors.forNone
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
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

    @Test
    fun `Request of edit page for not existing client id should return 404 error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId: Long = -1

        // When
        val document =
            therapist.clients.getEditClientCardPage(notExistingClientId, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage NotFoundErrorPage
    }

}