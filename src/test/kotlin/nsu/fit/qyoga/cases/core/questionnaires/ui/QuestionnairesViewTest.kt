package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

class QuestionnairesViewTest : QYogaAppTestBase() {

    @Test
    fun `QYoga returns questionnaire-list page with questionnaires`() {
        When {
            get("/questionnaires/")
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
        val request = QuestionnaireSearchDto(orderType = "DESK")
        Given {
            contentType(ContentType.JSON)
            body(request)
        } When {
            get("/questionnaires/action")
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
        val request = QuestionnaireSearchDto(title = "test", orderType = "DESK")
        Given {
            contentType(ContentType.JSON)
            body(request)
        } When {
            get("/questionnaires/action")
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
        val request = QuestionnaireSearchDto(title = "test", orderType = "DESK")
        Given {
            contentType(ContentType.JSON)
            body(request)
        } When {
            get("/questionnaires/action")
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
