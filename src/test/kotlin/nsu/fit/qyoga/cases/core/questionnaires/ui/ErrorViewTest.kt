package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


private const val USERNAME_FORM_PARAM = "username"
private const val PASSWORD_FORM_PARAM = "password"

class ErrorViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer
    lateinit var cookie: Cookie

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }

    @BeforeEach
    fun setupCookie() {
        cookie = Given {
            formParam(USERNAME_FORM_PARAM, "therapist")
            formParam(PASSWORD_FORM_PARAM, "diem-Synergy5")
        }.post("/users/login").thenReturn().detailedCookie("JSESSIONID")
    }

    @Test
    fun `QYoga can create new questionnaire`() {
        Given {
            cookie(cookie)
        } When {
            get("/questionnaires/new")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questions"){ exists() }
                node("#question1"){ exists() }
                node("#question1Header"){ exists() }
                node("#question1Body"){ exists() }
                node("#answer1"){ exists() }
            }
        }
    }
}