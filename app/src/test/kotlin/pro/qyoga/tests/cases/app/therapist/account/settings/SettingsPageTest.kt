package pro.qyoga.tests.cases.app.therapist.account.settings

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.account.SettingsPage


@DisplayName("Страница настроек терапевта")
class SettingsPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `должна корректно рендерится`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.settings.getSettingsPage()

        // Проверка
        document shouldBePage SettingsPage
    }

}