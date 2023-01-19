package nsu.fit.qyoga.infra

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import nsu.fit.qyoga.app.QYogaApplication
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration


//@ContextConfiguration(
//    classes = [QYogaApplication::class],
//    initializers = [TestContainerDbContextInitializer::class]
//)
//@SpringBootTest(
//    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
//)
@QYogaAppTest
class QYogaAppTestBase {

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        val logConfig = LogConfig.logConfig()
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
        val config = RestAssuredConfig.config().logConfig(logConfig)

        RestAssured.requestSpecification =  RequestSpecBuilder()
            .setBaseUri("http://localhost:$port")
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .setConfig(config)
            .build()
    }

}