package pro.qyoga.tests.cases.app.therapist

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.appointments.EmptyFutureSchedulePage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class TherapistMainPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Therapist's authenticated request to base path should be redirected to schedule page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.getIndexPage()

        // Then
        document shouldBePage EmptyFutureSchedulePage
    }

}