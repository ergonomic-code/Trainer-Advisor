package pro.qyoga.tests.cases.app.infra

import io.restassured.module.kotlin.extensions.Then
import org.hamcrest.CoreMatchers.startsWith
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class ProxyingTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Spring should respect X-Forwarded-For header`() {
        // Given
        val forwardedForProtocol = "https"

        // When
        val response = PublicClient.authApi.loginForVerification(
            THE_THERAPIST_LOGIN,
            THE_THERAPIST_PASSWORD,
            headers = mapOf("X-Forwarded-Proto" to forwardedForProtocol)
        )

        response.Then {
            statusCode(HttpStatus.FOUND.value())
            header("Location", startsWith(forwardedForProtocol))
        }
    }

}