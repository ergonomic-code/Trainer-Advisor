package pro.qyoga.tests.cases.app.therapist.clients

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.clients.pages.therapist.clients.journal.list.EmptyClientJournalPage
import pro.qyoga.tests.fixture.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest

class ClientViewPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Request of non existing client edit page should be forwarded to not found error page`() {
        // Given
        val notExistingClientId = -1L
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.getClientEditPage(notExistingClientId, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `Edit page for just created client should be rendered correctly and contain empty log`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientRequest = createClientCardDto()
        val client = backgrounds.clients.createClients(listOf(newClientRequest), THE_THERAPIST_ID).first()

        // When
        val document = therapist.clients.getClientEditPage(client.id)

        // Then
        document shouldBe EmptyClientJournalPage
    }

}