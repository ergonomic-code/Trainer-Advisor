package pro.qyoga.cases.app.publc

import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldBe
import pro.qyoga.clients.PublicClient
import pro.qyoga.clients.pages.publc.LoginPage
import pro.qyoga.infra.web.QYogaAppBaseTest


class UnauthorizedMainPageTest : QYogaAppBaseTest() {

    @Test
    fun `Unauthorized request to base path should be redirected to login page`() {
        // Given

        // When
        val document = PublicClient.getIndexPage()

        // Then
        document shouldBe LoginPage
    }

}