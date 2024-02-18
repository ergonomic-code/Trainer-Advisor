package pro.qyoga.tests.cases.app.error_handling

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.pages.publc.GenericErrorPage
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class ErrorHandlingTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `QYoga should return user friendly error page on unexpected error`() {
        // Given

        // When
        val document = PublicClient.getFailPage()

        // Then
        document shouldBe GenericErrorPage
    }

    @Test
    fun `QYoga should return user friendly error page on request of not existing page`() {
        // Given

        // When
        val document = PublicClient.getNotFoundPage()

        // Then
        document shouldBePage NotFoundErrorPage
    }

}