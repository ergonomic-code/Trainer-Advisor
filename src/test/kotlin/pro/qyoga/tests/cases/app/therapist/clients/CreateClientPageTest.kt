package pro.qyoga.tests.cases.app.therapist.clients

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.Test
import pro.qyoga.core.clients.api.DistributionSource
import pro.qyoga.core.clients.api.DistributionSourceType
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.clients.CreateClientPage
import pro.qyoga.tests.fixture.clients.ClientsObjectMother
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

}