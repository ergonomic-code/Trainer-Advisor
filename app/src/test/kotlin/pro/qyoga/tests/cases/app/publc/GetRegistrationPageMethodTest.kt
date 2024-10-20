package pro.qyoga.tests.cases.app.publc

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.RegisterPage


@DisplayName("Метод получения страницы регистрации")
class GetRegistrationPageMethodTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `должен возвращать корректную страницу регистрации`() {
        // Сетап

        // Действие
        val document = PublicClient.authApi.getRegistrationPage()

        // Проверка
        document shouldBe RegisterPage
    }

}