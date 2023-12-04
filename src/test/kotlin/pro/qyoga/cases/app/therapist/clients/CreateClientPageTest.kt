package pro.qyoga.cases.app.therapist.clients

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldBe
import pro.qyoga.assertions.shouldMatch
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.therapist.clients.CreateClientPage
import pro.qyoga.core.clients.api.DistributionSource
import pro.qyoga.core.clients.api.DistributionSourceType
import pro.qyoga.fixture.clients.ClientsObjectMother
import pro.qyoga.infra.web.QYogaAppBaseTest

class CreateClientPageTest : QYogaAppBaseTest() {

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