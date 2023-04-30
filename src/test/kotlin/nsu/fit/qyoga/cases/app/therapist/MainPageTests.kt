package nsu.fit.qyoga.cases.app.therapist

import io.github.ulfs.assertj.jsoup.Assertions
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class MainPageTests : QYogaAppTestBase() {

    @Test
    fun `Authenticated request to root url should redirect to main page`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/")
        } Then {
            statusCode(HttpStatus.OK.value())
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                // пока что главная страница не готова и вместо неё используется страница клиентов
                node("title") { hasText("Список клиентов") }
            }
        }
    }

}
