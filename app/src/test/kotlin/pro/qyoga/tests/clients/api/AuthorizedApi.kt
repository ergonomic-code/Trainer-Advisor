package pro.qyoga.tests.clients.api

import org.springframework.http.HttpCookie
import org.springframework.test.web.servlet.client.RestTestClient


interface AuthorizedApi {

    val authCookie: HttpCookie

    fun RestTestClient.RequestHeadersSpec<*>.authorized(): RestTestClient.RequestHeadersSpec<*> {
        return cookie(authCookie.name, authCookie.value)
    }

}
