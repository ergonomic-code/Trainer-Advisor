package pro.qyoga.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import pro.qyoga.clients.pages.publc.LoginPage


object AuthApi {

    fun login(login: String, password: String): Cookie {
        val cookie = Given {
            LoginPage
            formParam(LoginPage.LoginForm.username.name, login)
            formParam(LoginPage.LoginForm.password.name, password)
        } When {
            post(LoginPage.LoginForm.action.url)
        } Then {
            statusCode(HttpStatus.FOUND.value())
        } Extract {
            detailedCookie("JSESSIONID")
        }

        return cookie
    }

    fun loginForVerification(login: String, password: String): Response {
        return Given {
            formParam(LoginPage.LoginForm.username.name, login)
            formParam(LoginPage.LoginForm.password.name, password)
        } When {
            post(LoginPage.LoginForm.action.url)
        }
    }

}