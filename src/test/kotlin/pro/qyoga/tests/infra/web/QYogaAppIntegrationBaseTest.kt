package pro.qyoga.tests.infra.web

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach


open class QYogaAppIntegrationBaseTest : QYogaAppBaseTest() {

    protected val baseUri = "http://localhost:$port"

    @BeforeEach
    fun setupRestAssured() {
        val logConfig = LogConfig.logConfig()
        val config = RestAssuredConfig.config().logConfig(logConfig)

        RestAssured.requestSpecification = RequestSpecBuilder()
            .setBaseUri(baseUri)
            .setAccept(ContentType.HTML)
            .setContentType("application/x-www-form-urlencoded; charset=UTF-8")
            .setRelaxedHTTPSValidation()
            .setConfig(config)
            .build()
    }

}