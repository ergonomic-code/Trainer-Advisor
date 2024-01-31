package pro.qyoga.tests.cases.app.therapist.appointments.core

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.appointments.SchedulePage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class SchedulePageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Schedule page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getTrainingSchedulePage()

        // Then
        document shouldBePage SchedulePage
    }

}
