package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

class QuestionnairesViewTest : QYogaAppTestBase() {

    @Test
    fun `QYoga returns questionnaire-list page with questionnaires`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaires-list") { exists() }
                node("#questionnaires-sort-bar") { exists() }
                node("#questionnaires-find-bar") { exists() }
                node("#questionnaires-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page questionnaire-list then user change sort type`() {
        Given {
            authorized()
            contentType(ContentType.JSON)
            param("title", "test")
            param("sort", "title,desc")
        } When {
            get("/therapist/questionnaires/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaires-list") { exists() }
                node("#questionnaires-sort-bar") { exists() }
                node("#questionnaires-find-bar") { exists() }
                node("#questionnaires-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page questionnaire-list then user change title`() {
        Given {
            authorized()
            contentType(ContentType.JSON)
            param("title", "test")
        } When {
            get("/therapist/questionnaires/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaires-list") { exists() }
                node("#questionnaires-sort-bar") { exists() }
                node("#questionnaires-find-bar") { exists() }
                node("#questionnaires-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page questionnaire-list then user change page`() {
        Given {
            authorized()
            contentType(ContentType.JSON)
            param("title", "test")
            param("sort", "title,desc")
            param("page", 1)
        } When {
            get("/therapist/questionnaires/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaires-list") { exists() }
                node("#questionnaires-sort-bar") { exists() }
                node("#questionnaires-find-bar") { exists() }
                node("#questionnaires-navigation-bar") { exists() }
            }
        }
    }

}
