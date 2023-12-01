package pro.qyoga.cases.app.error_handling

import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldBe
import pro.qyoga.clients.PublicClient
import pro.qyoga.clients.pages.publc.GenericErrorPage
import pro.qyoga.clients.pages.publc.NotFoundErrorPage
import pro.qyoga.infra.web.QYogaAppBaseTest


class ErrorHandlingTest : QYogaAppBaseTest() {

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
        document shouldBe NotFoundErrorPage
    }

}