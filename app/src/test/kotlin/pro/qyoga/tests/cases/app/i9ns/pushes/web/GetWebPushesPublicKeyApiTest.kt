package pro.qyoga.tests.cases.app.i9ns.pushes.web

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.tests.clients.api.TrainerAdvisorApis
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.QYogaAppBaseTest

@DisplayName("Метод получения публичного ключа веб-пушей")
class GetWebPushesPublicKeyApiTest : QYogaAppBaseTest() {

    @Test
    fun `должен возвращать публичный ключ, указанный в конфигурации приложения`() {
        // Given
        val key = context.environment.getProperty("trainer-advisor.integrations.web-pushes.public-key")!!

        // When
        val response = TrainerAdvisorApis.WebPushes.publicApi.getPublicKey()

        // Then
        response shouldBe key
    }

}
