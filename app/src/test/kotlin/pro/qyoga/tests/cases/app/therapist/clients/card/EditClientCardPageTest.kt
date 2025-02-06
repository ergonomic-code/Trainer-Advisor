package pro.qyoga.tests.cases.app.therapist.clients.card

import io.kotest.inspectors.forAny
import io.kotest.inspectors.forNone
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.core.clients.cards.model.toUIFormat
import pro.qyoga.core.clients.cards.toDto
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.fixture.object_mothers.clients.randomPhoneNumber
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage


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
    fun `Edit client card page should be rendered correctly for minimal client`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(listOf(ClientsObjectMother.createClientCardDtoMinimal())).first()

        // When
        val document = therapist.clients.getEditClientCardPage(client.id)

        // Then
        document shouldBe EditClientPage.pageFor(client)
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
        val clients = backgrounds.clients.getAllClients()
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
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch editedMinimalClient }
    }

    @Test
    fun `Request of edit page for not existing client id should return 404 error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()

        // When
        val document =
            therapist.clients.getEditClientCardPage(notExistingClientId, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `Edit of not existing client should return 404 error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()

        // When
        val document =
            therapist.clients.editClientForError(
                notExistingClientId,
                createClientCardDto(),
                expectedStatus = HttpStatus.NOT_FOUND
            )

        // Then
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `System should return page with duplicated phone error message on posting form with duplicated phone`() {
        // Given
        val thePhone = randomPhoneNumber().toUIFormat()
        backgrounds.clients.createClient(phone = thePhone)
        val existingClient = backgrounds.clients.createClient()
        val existingClientDto = existingClient.toDto()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document =
            therapist.clients.editClientForError(existingClient.id, existingClientDto.copy(phoneNumber = thePhone))

        // Then
        document shouldHaveElement CreateClientForm.invalidPhoneInput
    }

    @Test
    fun `System should allow to change client phone to phone number of client of another therapist`() {
        // Given
        val thePhone = randomPhoneNumber().toUIFormat()
        val anotherTherapistId = backgrounds.users.registerNewTherapist().id
        backgrounds.clients.createClient(phone = thePhone, therapistId = anotherTherapistId)
        val targetClient = backgrounds.clients.createClient()
        val updatePhoneDto = targetClient.toDto().copy(phoneNumber = thePhone)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.editClient(targetClient.id, updatePhoneDto)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch updatePhoneDto }
    }

}