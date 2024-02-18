package pro.qyoga.tests.cases.app.auth

import com.fasterxml.jackson.databind.JsonNode
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.restassured.http.ContentType
import io.restassured.http.Cookie
import io.restassured.matcher.RestAssuredMatchers.detailedCookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.OpsClient
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.actuatorPath
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_ADMIN_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_ADMIN_PASSWORD
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


class AuthTests : QYogaAppIntegrationBaseTest() {

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
            .minusSeconds(1) // без этого тест мигает

        // When
        val response = PublicClient.authApi.loginForVerification(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)

        response.Then {
            statusCode(HttpStatus.FOUND.value())
            cookie(
                "remember-me",
                detailedCookie()
                    .expiryDate(greaterThanOrEqualTo(Date(now.plus(9, ChronoUnit.DAYS).toEpochMilli())))
            )
        }
    }

    @Test
    fun `TA should allow access with credentials provided via basic authentication`() {
        Given {
            auth().preemptive().basic(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)
        } When {
            get(ClientsListPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            val document = Jsoup.parse(body().asString())
            document shouldBePage ClientsListPage
        }
    }

    @Test
    fun `TA should prohibit unauthenticated access to root endpoints that aren't opened explicitly`() {
        Given {
            // no auth
            this
        } When {
            get("it-isnt-opened")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            val document = Jsoup.parse(body().asString())
            document shouldBePage LoginPage
        }
    }

    @Test
    fun `TA should prohibit access to actuator without authentication`() {
        Given {
            // no auth
            this
        } When {
            get(actuatorPath)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            val document = Jsoup.parse(body().asString())
            document shouldBePage LoginPage
        }
    }

    @Test
    fun `TA should prohibit access to actuator with therapist credentials`() {
        Given {
            auth()
                .preemptive()
                .basic(THE_THERAPIST_LOGIN, THE_THERAPIST_LOGIN)
        } When {
            get(actuatorPath)
        } Then {
            statusCode(HttpStatus.UNAUTHORIZED.value())
        }
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
        Given {
            // no auth
            accept(ContentType.ANY)
            this
        } When {
            get("/fonts/inter/Inter-Regular.woff2")
        } Then {
            statusCode(HttpStatus.OK.value())
        }
    }

}
