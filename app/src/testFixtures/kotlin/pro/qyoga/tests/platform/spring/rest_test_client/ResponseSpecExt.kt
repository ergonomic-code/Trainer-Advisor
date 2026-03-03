package pro.qyoga.tests.platform.spring.rest_test_client

import org.springframework.test.web.servlet.client.RestTestClient
import java.net.URI


fun RestTestClient.ResponseSpec.getBodyAsString(): String =
    String(this.returnResult().responseBodyContent)

fun RestTestClient.ResponseSpec.redirectLocation(): URI =
    this
        .expectStatus().isFound.returnResult()
        .responseHeaders
        .location!!
