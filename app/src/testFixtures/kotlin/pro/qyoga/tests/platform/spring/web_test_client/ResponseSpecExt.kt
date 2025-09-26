package pro.qyoga.tests.platform.spring.web_test_client

import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import java.net.URI


fun WebTestClient.ResponseSpec.getBodyAsString(): String =
    this.returnResult(String::class.java)
        .responseBody
        .collectList()
        .block()!!
        .joinToString("\n")

fun WebTestClient.ResponseSpec.redirectLocation(): URI =
    this
        .expectStatus().isFound.returnResult<Unit>()
        .responseHeaders
        .location!!