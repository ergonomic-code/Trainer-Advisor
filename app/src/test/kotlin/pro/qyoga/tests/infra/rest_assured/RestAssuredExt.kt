package pro.qyoga.tests.infra.rest_assured

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.springframework.context.ConfigurableApplicationContext
import pro.qyoga.tests.infra.test_config.spring.baseUrl


fun configureRestAssured(context: ConfigurableApplicationContext) {
    val logConfig = LogConfig.logConfig()
    val config = RestAssuredConfig.config().logConfig(logConfig)

    RestAssured.requestSpecification = RequestSpecBuilder()
        .setBaseUri(context.baseUrl)
        .setAccept(ContentType.HTML)
        .setContentType("application/x-www-form-urlencoded; charset=UTF-8")
        .setRelaxedHTTPSValidation()
        .setConfig(config)
        .build()
}

fun RequestSpecification.addRequestLogging(): RequestSpecification =
    filter(RequestLoggingFilter())

fun RequestSpecification.addResponseLogging(): RequestSpecification =
    filter(ResponseLoggingFilter())
