package pro.qyoga.cases.app.publc

import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import org.hamcrest.Matchers.matchesRegex
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.assertions.shouldHave
import pro.qyoga.clients.PublicClient
import pro.qyoga.clients.pages.publc.LoginPage
import pro.qyoga.fixture.THE_THERAPIST_LOGIN
import pro.qyoga.fixture.THE_THERAPIST_PASSWORD
import pro.qyoga.infra.web.QYogaAppBaseTest


class LoginPageTest : QYogaAppBaseTest() {

    @Test
    fun `After login with valid credentials user should be redirected to index page`() {
        // Given

        // When
        val response = PublicClient.authApi.loginForVerification(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)

        response.Then {
            statusCode(HttpStatus.FOUND.value())
            header("location", matchesRegex(".*:\\d{4,5}/"))
        }
    }

    @Test
    fun `After login with invalid credentials user should be redirected to login page with error message`() {
        // Given

        // When
        val response = PublicClient.authApi.loginForVerification(THE_THERAPIST_LOGIN, "invalid login")

        val page = response.Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }

        page shouldHave LoginPage.LoginForm.invalidUserName
    }

}