package pro.qyoga.tests.platform.spring.rest_test_client

import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.client.RestTestClient
import java.net.URI
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


fun RestTestClient.ResponseSpec.getBodyAsString(): String =
    String(this.returnResult().responseBodyContent)

fun RestTestClient.ResponseSpec.redirectLocation(): URI =
    this
        .expectStatus().isFound.returnResult()
        .responseHeaders
        .location!!

fun RestTestClient.ResponseSpec.cookieExpiresAt(name: String): Instant {
    val cookie = returnResult().responseHeaders[HttpHeaders.SET_COOKIE]!!.first { it.startsWith("$name=") }
    val expires = cookie.split(';').first { it.trim().startsWith("Expires=", ignoreCase = true) }
        .substringAfter('=')
    return ZonedDateTime.parse(expires, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant()
}
