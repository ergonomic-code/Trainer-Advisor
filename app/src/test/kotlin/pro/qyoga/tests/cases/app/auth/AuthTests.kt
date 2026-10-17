package pro.qyoga.tests.cases.app.auth

import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.http.HttpCookie
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.OpsClient
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_ADMIN_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_ADMIN_PASSWORD
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.platform.spring.rest_test_client.cookieExpiresAt
import tools.jackson.databind.JsonNode
import java.time.Instant
import java.time.temporal.ChronoUnit


class AuthTests : QYogaAppIntegrationBaseTest() {

    @Test
    fun `When unauthenticated user requests secured page he should be redirected to login page`() {
        // Given
        val therapist = TherapistClient(HttpCookie("unauthorized", ""))

        // When
        val response = therapist.clients.getClientsListPage()

        // Then
        response shouldBe LoginPage
    }

    @Test
    fun `TA should remember users for 9 days`() {
        // Given
        val now = Instant.now()
            .minusSeconds(1) // без этого тест мигает

        // When
        val response = PublicClient.authApi.loginForVerification(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)

        // Then
        response.expectStatus().isFound
        response.cookieExpiresAt("remember-me") shouldBeGreaterThanOrEqualTo now.plus(9, ChronoUnit.DAYS)
    }

    @Test
    fun `TA should not allow access to app with credentials provided via basic authentication`() {
        // Given

        // When
        val document = PublicClient.getClientsPageWithBasicAuth(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)

        // Then
        document shouldBePage LoginPage
    }

    @Test
    fun `TA should prohibit unauthenticated access to root endpoints that aren't opened explicitly`() {
        // Given

        // When
        val document = PublicClient.getUnopenedPage()

        // Then
        document shouldBePage LoginPage
    }

    @Test
    fun `TA should prohibit access to actuator without authentication`() {
        // Given

        // When
        val response = PublicClient.getActuatorEntryPointForResponse()

        // Then
        response.expectStatus().isUnauthorized
    }

    @Test
    fun `TA should prohibit access to actuator with therapist credentials`() {
        // Given
        val therapist = OpsClient(THE_THERAPIST_LOGIN, THE_THERAPIST_LOGIN)

        // When
        val response = therapist.getActuatorEntryPointForResponse()

        // Then
        response.expectStatus().isUnauthorized
    }

    @Test
    fun `TA should allow access to actuator with admin credentials`() {
        // Given
        val admin = OpsClient(THE_ADMIN_LOGIN, THE_ADMIN_PASSWORD)

        // When
        val resp = admin.getActuatorEntryPoint()

        // Then
        resp shouldNotBe null
        resp!!["_links"]["env"].shouldBeInstanceOf<JsonNode>()
    }

    @Test
    fun `TA should allow access to fonts endpoints without authentication`() {
        // Given

        // When
        val response = PublicClient.getFontForResponse()

        // Then
        response.expectStatus().isOk
    }

}
