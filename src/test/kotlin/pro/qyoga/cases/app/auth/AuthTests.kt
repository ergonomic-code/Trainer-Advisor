package pro.qyoga.cases.app.auth

import io.restassured.http.Cookie
import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldBe
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.publc.LoginPage
import pro.qyoga.infra.web.QYogaAppBaseTest


class AuthTests : QYogaAppBaseTest() {

    @Test
    fun `When unauthenticated user requests secured page he should be redirected to login page`() {
        // Given
        val therapist = TherapistClient(-1, Cookie.Builder("unauthorized").build())

        // When
        val response = therapist.clients.getClientsListPage()

        // Then
        response shouldBe LoginPage
    }

}