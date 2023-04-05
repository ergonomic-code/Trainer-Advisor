package nsu.fit.qyoga.cases.app.external

import io.github.ulfs.assertj.jsoup.Assertions
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.hamcrest.core.StringEndsWith.endsWith
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

private const val USERNAME_FORM_PARAM = "username"
private const val PASSWORD_FORM_PARAM = "password"

class AuthorizationTests : QYogaAppTestBase() {

    @Test
    fun `Unauthorized access to therapist's page should be restricted`() {
        Given {
            this
        } When {
            get("/clients")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("title") { hasText("Вход в систему") }
                node("#loginForm") { exists() }
                node("#loginForm input[name=$USERNAME_FORM_PARAM]") { exists() }
                node("#loginForm input[name=$PASSWORD_FORM_PARAM]") { exists() }
            }
        }
    }

    @Test
    fun `authorized access to therapist's page should be allowed`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/clients")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("title") { hasText("Список клиентов") }
            }
        }
    }

    @Test
    fun `When user provides valid credentials he should be redirected to main page`() {
        Given {
            formParam(USERNAME_FORM_PARAM, "admin")
            formParam(PASSWORD_FORM_PARAM, "diem-Synergy5")
        } When {
            post("/users/login")
        } Then {
            statusCode(302)
            header("Location", endsWith("clients"))
        }
    }

    @Test
    fun `When user provides invalid password an error page should be displayed`() {
        Given {
            formParam(USERNAME_FORM_PARAM, "admin")
            formParam(PASSWORD_FORM_PARAM, "fail-password")
        } When {
            post("/users/login")
        } Then {
            statusCode(200)
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#errorText") { hasText("Неверный пароль") }
            }
        }
    }

    @Test
    fun `When user provides no credentials an error page should be displayed`() {
        Given {
            formParam(USERNAME_FORM_PARAM, "")
            formParam(PASSWORD_FORM_PARAM, "")
        } When {
            post("/users/login")
        } Then {
            statusCode(200)
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#errorText") { hasText("Неверный логин") }
            }
        }
    }

    @Test
    fun `When user provides invalid login an error page should be displayed`() {
        Given {
            formParam(USERNAME_FORM_PARAM, "admin1")
            formParam(PASSWORD_FORM_PARAM, "diem-Synergy5")
        } When {
            post("/users/login")
        } Then {
            statusCode(200)
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#errorText") { hasText("Неверный логин") }
            }
        }
    }

    @Test
    fun `When user provides invalid credentials an error page should be displayed`() {
        Given {
            formParam(USERNAME_FORM_PARAM, "admin1")
            formParam(PASSWORD_FORM_PARAM, "fail-password")
        } When {
            post("/users/login")
        } Then {
            statusCode(200)
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#errorText") { hasText("Неверный логин") }
            }
        }
    }
}
