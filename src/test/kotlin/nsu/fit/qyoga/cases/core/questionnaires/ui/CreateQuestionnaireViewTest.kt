package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreateQuestionnaireViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-empty-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can edit questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/1/edit")
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questions") { exists() }
                node("#questionnaireTitle") {
                    attribute("value") { hasText("title") }
                }
                node("#question0") { notExists() }
                node("#question0Header") { notExists() }
                node("#question0Body") { notExists() }
                node("#answer0") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga can create new questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questions") { exists() }
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can update questionnaire title`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit")
            param("id", 1)
            param("title", "asd")
            post("/therapist/questionnaires/edit/title")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga can't update questionnaire title if it not in session`() {
        Given {
            authorized()
        } When {
            param("id", 1)
            param("title", "asd")
            post("/therapist/questionnaires/edit/title")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can get questionnaire edit page`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questions") { exists() }
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can save question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit")
            param("id", "1")
            param("title", "test")
            param("questions[0].id", "1")
            param("questions[0].questionnaireId", "1")
            param("questions[0].title", "questions title")
            param("questions[0].questionType", "SINGLE")
            param("questions[0].answers[0].id", "1")
            param("questions[0].answers[0].questionId", "1")
            param("questions[0].answers[0].score", "1")
            param("questions[0].answers[0].title", "answer title")
            post("/therapist/questionnaires/edit")
        } Then {
            extract().statusCode().compareTo(302) shouldBe 0
        }
    }
}
