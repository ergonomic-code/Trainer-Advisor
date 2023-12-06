package pro.qyoga.tests.cases.app.therapist

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class TherapistMainPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Therapist's authenticated request to base path should be redirected to clients page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.getIndexPage()

        // Then
        document shouldBe ClientsListPage
    }

}