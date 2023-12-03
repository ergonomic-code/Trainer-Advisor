package pro.qyoga.cases.app.therapist.clients

import io.kotest.inspectors.forAny
import io.kotest.inspectors.forNone
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.assertions.shouldBe
import pro.qyoga.assertions.shouldHave
import pro.qyoga.assertions.shouldMatch
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.publc.NotFoundErrorPage
import pro.qyoga.clients.pages.therapist.clients.EditClientPage
import pro.qyoga.fixture.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.infra.web.QYogaAppBaseTest

class EditClientPageTest : QYogaAppBaseTest() {

    @Test
    fun `Request of non existing client edit page should be forwarded to not found error page`() {
        // Given
        val notExistingClientId = -1L
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.getClientEditPage(notExistingClientId, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBe NotFoundErrorPage
    }

    @Test
    fun `Edit client page should be rendered correctly and has prefilled form`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientRequest = createClientCardDto()
        val client = backgrounds.clients.createClients(listOf(newClientRequest), THE_THERAPIST_ID).first()
        val editClientPage = EditClientPage(client.id)

        // When
        val document = therapist.clients.getClientEditPage(client.id)

        // Then
        document shouldBe editClientPage
        document shouldHave editClientPage.clientForm(client)
    }

    @Test
    fun `Client editing should be persistent`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        val newClientCardDto = createClientCardDto()
        val client = backgrounds.clients.createClients(listOf(newClientCardDto), THE_THERAPIST_ID).first()

        val editedClientCardDto = createClientCardDto()

        // When
        therapist.clients.editClient(client.id, editedClientCardDto)

        // Then
        backgrounds.clients.getAllClients().content.forNone { it shouldMatch newClientCardDto }
        backgrounds.clients.getAllClients().content.forAny { it shouldMatch editedClientCardDto }
    }

}