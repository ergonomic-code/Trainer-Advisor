package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.restassured.http.ContentType
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

class QuestionnairesViewTest : QYogaAppTestBase() {

    @Autowired
    lateinit var dbInitializer: DbInitializer
    lateinit var cookie: Cookie

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "/db/questionnaires/questionnaires-insert-data-script.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga returns questionnaire-list page with questionnaires`() {
        Given {
            cookie(cookie)
        } When {
            get("/questionnaires")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaires-list") {
                    hasText("test1 test10 test11 test12 test2 test3 test4 test5 test6 test7")
                }
                node("#questionnaires-sort-bar") { exists() }
                node("#questionnaires-find-bar") { exists() }
                node("#questionnaires-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page questionnaire-list then user change sort type`() {
        Given {
            cookie(cookie)
            contentType(ContentType.JSON)
            param("title", "test")
            param("sort", "title,desc")
        } When {
            get("/questionnaires/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaires-list") {
                    hasText("test9 test8 test7 test6 test5 test4 test3 test2 test12 test11")
                }
                node("#questionnaires-sort-bar") { exists() }
                node("#questionnaires-find-bar") { exists() }
                node("#questionnaires-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page questionnaire-list then user change title`() {
        Given {
            cookie(cookie)
            contentType(ContentType.JSON)
            param("title", "test")
        } When {
            get("/questionnaires/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaires-list") {
                    hasText("test1 test10 test11 test12 test2 test3 test4 test5 test6 test7")
                }
                node("#questionnaires-sort-bar") { exists() }
                node("#questionnaires-find-bar") { exists() }
                node("#questionnaires-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page questionnaire-list then user change page`() {
        Given {
            cookie(cookie)
            contentType(ContentType.JSON)
            param("title", "test")
            param("sort", "title,desc")
            param("page", 1)
        } When {
            get("/questionnaires/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaires-list") {
                    hasText("test10 test1")
                }
                node("#questionnaires-sort-bar") { exists() }
                node("#questionnaires-find-bar") { exists() }
                node("#questionnaires-navigation-bar") { exists() }
            }
        }
    }

}
