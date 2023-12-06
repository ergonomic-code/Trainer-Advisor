package pro.qyoga.tests.cases.app.therapist.fragments

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldHaveValidLink
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class NavBarLinksTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Links in left and top nav bars should lead to existing urls`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.getClientsListPage()

        // Then

        // left nav bar
        document.shouldHaveValidLink("Главная", therapist.authCookie)
        document.shouldHaveValidLink("Клиенты", therapist.authCookie)
        document.shouldHaveValidLink("Упражнения", therapist.authCookie)

        // top nav bar
        document.shouldHaveValidLink("Выйти", therapist.authCookie)
    }

}