package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.pages.publc.RegisterPage


object AuthApi {

    fun login(login: String, password: String): Cookie {
        val cookie = Given {
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

    fun loginForVerification(
        login: String,
        password: String,
        headers: Map<String, *> = emptyMap<String, Any>()
    ): Response {
        return Given {
            headers(headers)
            formParam(LoginPage.LoginForm.username.name, login)
            formParam(LoginPage.LoginForm.password.name, password)
        } When {
            post(LoginPage.LoginForm.action.url)
        }
    }

    fun getRegistrationPage(): Document {
        return Given {
            this
        } When {
            get(RegisterPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun registerTherapist(registerTherapistRequest: RegisterTherapistRequest): Document {
        return Given {
            formParam(RegisterPage.RegisterForm.firstName.name, registerTherapistRequest.firstName)
            formParam(RegisterPage.RegisterForm.lastName.name, registerTherapistRequest.lastName)
            formParam(RegisterPage.RegisterForm.email.name, registerTherapistRequest.email)
        } When {
            post(RegisterPage.RegisterForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}