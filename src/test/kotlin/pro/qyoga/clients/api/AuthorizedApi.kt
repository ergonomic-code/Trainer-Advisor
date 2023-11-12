package pro.qyoga.clients.api

import io.restassured.http.Cookie
import io.restassured.specification.RequestSpecification


interface AuthorizedApi {

    val authCookie: Cookie

    fun RequestSpecification.authorized(): RequestSpecification {
        return cookie(authCookie)
    }

}