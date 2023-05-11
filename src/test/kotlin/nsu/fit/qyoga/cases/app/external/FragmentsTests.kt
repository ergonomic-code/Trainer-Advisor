package nsu.fit.qyoga.cases.app.external

import io.github.ulfs.assertj.jsoup.Assertions
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus


class FragmentsTests : QYogaAppTestBase() {

    @Test
    fun `header fragment should be placed inside head tag`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/")
        } Then {
            statusCode(HttpStatus.OK.value())
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                // пока что главная страница не готова и вместо неё используется страница клиентов
                node("html>head>title") { exists() }
            }
        }
    }

}