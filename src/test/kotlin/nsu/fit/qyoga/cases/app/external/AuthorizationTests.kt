package nsu.fit.qyoga.cases.app.external

import io.github.ulfs.assertj.jsoup.Assertions
import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.hamcrest.core.StringEndsWith.endsWith
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

class AuthorizationTests : QYogaAppTestBase() {
    @Test
    fun `Unauthorized access to therapist's page should be restricted`() {
        Given {
            this
        } When {
            get("/main")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("title") { hasText("Вход в систему") }
                node("#loginForm") { exists() }
            }
        }
    }

    @Test
    fun `authorized access to therapist's page should be allowed`() {
        val cookie: Cookie
        cookie = Given {
            formParam("username", "admin")
            formParam("password", "diem-Synergy5")
        }.post("/users/login").thenReturn().detailedCookie("JSESSIONID")

        Given {
            this.cookie(cookie)
        } When {
            get("/main")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("title") { hasText("QYoga") }
            }
        }
    }

    @Test
    fun `When user provides valid credentials he should be redirected to main page`() {
        Given {
            formParam("username", "admin")
            formParam("password", "diem-Synergy5")
        } When {
            post("/users/login")
        } Then {
            statusCode(302)
            header("Location", endsWith("main"))
        }
    }

    @Test
    fun `When user provides invalid password an error page should be displayed`() {
        Given {
            formParam("username", "admin")
            formParam("password", "fail-password")
        } When {
            post("/users/login")
        } Then {
            statusCode(302)
            header("Location", endsWith("error"))
        }
    }

    @Test
    fun `When user provides no credentials an error page should be displayed`() {
        Given {
            formParam("username", "")
            formParam("password", "")
        } When {
            post("/users/login")
        } Then {
            statusCode(302)
            header("Location", endsWith("error"))
        }
    }

    @Test
    fun `When user provides invalid login an error page should be displayed`() {
        Given {
            formParam("username", "admin1")
            formParam("password", "diem-Synergy5")
        } When {
            post("/users/login")
        } Then {
            statusCode(302)
            header("Location", endsWith("error"))
        }
    }

    @Test
    fun `When user provides invalid credentials an error page should be displayed`() {
        Given {
            formParam("username", "admin1")
            formParam("password", "fail-password")
        } When {
            post("/users/login")
        } Then {
            statusCode(302)
            header("Location", endsWith("error"))
        }
    }
}
