package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.specification.RequestSpecification
import org.springframework.test.web.servlet.client.RestTestClient


interface AuthorizedApi {

    val authCookie: Cookie

    fun RequestSpecification.authorized(): RequestSpecification {
        return cookie(authCookie)
    }

    fun RestTestClient.RequestHeadersSpec<*>.authorized(): RestTestClient.RequestHeadersSpec<*> {
        return cookie(authCookie.name, authCookie.value)
    }

}
