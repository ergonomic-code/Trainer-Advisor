package pro.qyoga.cases.app.auth

import io.restassured.http.Cookie
import io.restassured.matcher.RestAssuredMatchers.detailedCookie
import io.restassured.module.kotlin.extensions.Then
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.assertions.shouldBe
import pro.qyoga.clients.PublicClient
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.publc.LoginPage
import pro.qyoga.fixture.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.fixture.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.infra.web.QYogaAppBaseTest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


class AuthTests : QYogaAppBaseTest() {

    @Test
    fun `When unauthenticated user requests secured page he should be redirected to login page`() {
        // Given
        val therapist = TherapistClient(Cookie.Builder("unauthorized").build())

        // When
        val response = therapist.clients.getClientsListPage()

        // Then
        response shouldBe LoginPage
    }

    @Test
    fun `TA should remember users for 9 days`() {
        // Given
        val now = Instant.now()

        // When
        val response = PublicClient.authApi.loginForVerification(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)

        response.Then {
            statusCode(HttpStatus.FOUND.value())
            cookie(
                "remember-me", detailedCookie()
                    .expiryDate(Matchers.greaterThan(Date(now.plus(9, ChronoUnit.DAYS).toEpochMilli())))
            )
        }
    }

}