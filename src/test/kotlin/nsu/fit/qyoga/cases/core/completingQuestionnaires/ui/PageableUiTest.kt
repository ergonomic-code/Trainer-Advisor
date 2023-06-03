package nsu.fit.qyoga.cases.core.completingQuestionnaires.ui

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

class PageableUiTest : QYogaAppTestBase() {

    @Test
    fun `QYoga returns empty page completing-list if nothing exists`() {
        Given {
            authorized()
            contentType(ContentType.JSON)
            param("page", 0)
        } When {
            get("/therapist/questionnaires/performing/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") { exists() }
                node(".table-completing-row") { notExists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }
}
