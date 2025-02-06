package pro.qyoga.tests.cases.app.therapist.clients.card

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.Test
import pro.qyoga.core.clients.cards.model.DistributionSource
import pro.qyoga.core.clients.cards.model.DistributionSourceType
import pro.qyoga.core.clients.cards.model.toUIFormat
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.randomPhoneNumber
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientPage

class CreateClientPageTest : QYogaAppIntegrationBaseTest() {

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
        val newClientRequest = ClientsObjectMother.createClientCardDto()

        // When
        therapist.clients.createClient(newClientRequest)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch newClientRequest }
    }

    @Test
    fun `Null distribution source comment should be persisted as null`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientRequest = ClientsObjectMother.createClientCardDto(
            distributionSource = DistributionSource(DistributionSourceType.OTHER, null)
        )

        // When
        therapist.clients.createClient(newClientRequest)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch newClientRequest }
    }

    @Test
    fun `System should accept create client forms containing only required fields`() {
        // Given
        val minimalClient = ClientsObjectMother.createClientCardDtoMinimal()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.createClient(minimalClient)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch minimalClient }
    }

    @Test
    fun `System should return with duplicated phone error message on posting form with duplicated phone`() {
        // Given
        val thePhone = randomPhoneNumber().toUIFormat()
        backgrounds.clients.createClient(phone = thePhone)
        val duplicatedClient = ClientsObjectMother.createClientCardDto(phone = thePhone)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.createClientForError(duplicatedClient)

        // Then
        document shouldHaveElement CreateClientForm.invalidPhoneInput
    }

    @Test
    fun `System should allow to create client with existing phone number for another therapist`() {
        // Given
        val thePhone = randomPhoneNumber().toUIFormat()
        val anotherTherapistId = backgrounds.users.registerNewTherapist().id
        backgrounds.clients.createClient(phone = thePhone, therapistId = anotherTherapistId)
        val duplicatedClient = ClientsObjectMother.createClientCardDto(phone = thePhone)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.createClient(duplicatedClient)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch duplicatedClient }
    }

}