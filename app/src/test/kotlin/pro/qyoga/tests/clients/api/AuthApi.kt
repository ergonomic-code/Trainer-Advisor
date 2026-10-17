package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.tests.infra.web.mainRestTestClient
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.pages.publc.RegisterPage
import pro.qyoga.tests.platform.spring.rest_test_client.formData
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString


object AuthApi {

    fun login(login: String, password: String): HttpCookie {
        return loginForVerification(login, password)
            .expectStatus().isFound
            .returnResult().responseCookies.getFirst("JSESSIONID")!!
    }

    fun loginForVerification(
        login: String,
        password: String,
        headers: Map<String, *> = emptyMap<String, Any>()
    ): RestTestClient.ResponseSpec {
        return mainRestTestClient.post()
            .uri(LoginPage.LoginForm.action.url)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .headers { httpHeaders -> headers.forEach { (name, value) -> httpHeaders.add(name, value.toString()) } }
            .body(formData(
                LoginPage.LoginForm.username.name to login,
                LoginPage.LoginForm.password.name to password
            ))
            .exchange()
    }

    fun getRegistrationPage(): Document {
        return pageRestTestClient.get()
            .uri(RegisterPage.path)
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun registerTherapist(registerTherapistRequest: RegisterTherapistRequest): Document {
        return mainRestTestClient.post()
            .uri(RegisterPage.RegisterForm.action.url)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData(
                RegisterPage.RegisterForm.firstName.name to registerTherapistRequest.firstName,
                RegisterPage.RegisterForm.lastName.name to registerTherapistRequest.lastName,
                RegisterPage.RegisterForm.email.name to registerTherapistRequest.email,
                RegisterPage.RegisterForm.captchaId.name to registerTherapistRequest.captchaAnswer.captchaId,
                RegisterPage.RegisterForm.captchaCode.name to registerTherapistRequest.captchaAnswer.captchaCode
            ))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

}
