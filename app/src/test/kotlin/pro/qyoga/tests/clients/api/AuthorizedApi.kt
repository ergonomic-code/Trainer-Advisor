package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.specification.RequestSpecification
import org.springframework.test.web.reactive.server.WebTestClient


interface AuthorizedApi {

    val authCookie: Cookie

    fun RequestSpecification.authorized(): RequestSpecification {
        return cookie(authCookie)
    }

    fun WebTestClient.RequestHeadersSpec<*>.authorized(): WebTestClient.RequestHeadersSpec<*> {
        return cookie(authCookie.name, authCookie.value)
    }

}