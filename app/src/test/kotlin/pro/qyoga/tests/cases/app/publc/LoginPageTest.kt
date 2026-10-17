package pro.qyoga.tests.cases.app.publc

import io.kotest.assertions.withClue
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString


class LoginPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `After login with valid credentials user should be redirected to index page`() {
        // Given

        // When
        val response = PublicClient.authApi.loginForVerification(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)

        // Then
        response.expectStatus().isFound
            .expectHeader().valueMatches("location", ".*:\\d{4,5}/therapist")
    }

    @Test
    fun `After login with invalid credentials user should be redirected to login page with error message`() {
        // Given

        // When
        val response = PublicClient.authApi.loginForVerification(THE_THERAPIST_LOGIN, "invalid login")

        // Then
        val page = response.expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }

        withClue("Cannot find error message by ${LoginPage.LoginForm.invalidUserName}") {
            page shouldHaveElement LoginPage.LoginForm.invalidUserName
        }
    }

    @Test
    fun `After login with valid credentials of disabled account, user should be redirected to login page with error message`() {
        // Given
        backgrounds.users.disable(THE_THERAPIST_LOGIN)

        // When
        val response = PublicClient.authApi.loginForVerification(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)

        // Then
        val page = response.expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }

        page shouldHaveElement LoginPage.LoginForm.invalidUserName
    }

}