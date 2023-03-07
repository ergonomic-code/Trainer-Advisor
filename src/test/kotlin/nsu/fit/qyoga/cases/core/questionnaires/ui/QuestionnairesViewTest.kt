package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.utils.OrderType
import nsu.fit.qyoga.core.questionnaires.utils.Page
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

class QuestionnairesViewTest : QYogaAppTestBase(){

    @Test
    fun `QYoga returns questionnaire-list page with questionnaires`() {
        When {
            get("/questionnaires/")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaire-list") { exists() }
                node("#questionnaire-sort-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#questionnaire-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns questionnaire list, navigation elements then user change sort type`() {
        val request = QuestionnaireSearchDto(title = null, page = Page(orderType = OrderType.DESK))
        Given {
            contentType(ContentType.JSON)
            body(request)
        } When {
            post("/questionnaires/sort")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaire-list") { exists() }
                node("#questionnaire-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns questionnaire list, navigation elements then user change search title`() {
        val request = QuestionnaireSearchDto(title = "test", page = Page(orderType = OrderType.DESK))
        Given {
            contentType(ContentType.JSON)
            body(request)
        } When {
            post("/questionnaires/sort")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaire-list") { exists() }
                node("#questionnaire-navigation-bar") { exists() }
            }
        }
    }

}
