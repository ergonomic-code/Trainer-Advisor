package pro.qyoga.tests.infra.rest_assured

import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification


fun RequestSpecification.addRequestLogging(): RequestSpecification =
    filter(RequestLoggingFilter())

fun RequestSpecification.addResponseLogging(): RequestSpecification =
    filter(ResponseLoggingFilter())
