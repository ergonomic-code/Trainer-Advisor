package pro.qyoga.cases.app.therapist

import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldBe
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.infra.web.QYogaAppBaseTest


class TherapistMainPageTest : QYogaAppBaseTest() {

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