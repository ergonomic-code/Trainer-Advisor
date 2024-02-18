package pro.qyoga.tests.cases.app.publc

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class UnauthorizedMainPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Unauthorized request to base path should be redirected to login page`() {
        // Given

        // When
        val document = PublicClient.getIndexPage()

        // Then
        document shouldBe LoginPage
    }

}