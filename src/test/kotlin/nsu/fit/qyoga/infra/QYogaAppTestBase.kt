package nsu.fit.qyoga.infra

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.web.server.LocalServerPort

@QYogaAppTest
class QYogaAppTestBase {

    @LocalServerPort
    var port: Int = 0

    val authCookie by lazy { authorize() }

    fun authorize(): Cookie {
        val cookie = Given {
            formParam("username", "therapist")
            formParam("password", "diem-Synergy5")
        } When {
            post("/login")
        } Extract {
            detailedCookie("JSESSIONID")
        }

        return cookie
    }

    fun RequestSpecification.authorized(): RequestSpecification {
        return this.cookie(authCookie)
    }

    @BeforeEach
    fun setup() {
        val logConfig = LogConfig.logConfig()
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
        val config = RestAssuredConfig.config().logConfig(logConfig)

        RestAssured.requestSpecification = RequestSpecBuilder()
            .setBaseUri("http://localhost:$port")
            .setContentType("application/x-www-form-urlencoded; charset=UTF-8")
            .setRelaxedHTTPSValidation()
            .setConfig(config)
            .build()
    }

}
