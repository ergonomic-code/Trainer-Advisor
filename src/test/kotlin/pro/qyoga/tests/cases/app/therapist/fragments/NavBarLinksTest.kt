package pro.qyoga.tests.cases.app.therapist.fragments

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.fragments.LeftNavBarFragment
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class NavBarLinksTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Links in left and top nav bars should lead to existing urls`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.getClientsListPage()

        // Then
        document shouldHaveComponent LeftNavBarFragment
    }

}