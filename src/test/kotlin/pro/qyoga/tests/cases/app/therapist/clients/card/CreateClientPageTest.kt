package pro.qyoga.tests.cases.app.therapist.clients.card

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.Test
import pro.qyoga.core.clients.cards.api.DistributionSource
import pro.qyoga.core.clients.cards.api.DistributionSourceType
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientPage
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest

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
        clients.content.forAny { it shouldMatch newClientRequest }
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
        clients.content.forAny { it shouldMatch newClientRequest }
    }

    @Test
    fun `System should accept create client forms containing only required fields`() {
        // Given
        val minimalClient = ClientsObjectMother.createClientCardDtoMinimal()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.createClient(minimalClient)

        // Then
        val clients = backgrounds.clients.getAllClients().content
        clients.forAny { it shouldMatch minimalClient }
    }

}