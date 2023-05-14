package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ErrorTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource",
        )
    }

    /*@Test
    fun `QYoga can not parse an object with a nested list of other objects`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/questionnaires/1/setResult/addResult")
            param("decodingDtoList[0].id", "1")
            param("decodingDtoList[0].questionnaireId", "1")
            param("decodingDtoList[0].lowerBound", "1")
            param("decodingDtoList[0].upperBound", "5")
            param("decodingDtoList[0].result", "test")
            post("/questionnaires/setResult")
            get("/questionnaires/1/setResult")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".decodingLowerBound") {
                    exists()
                    attribute("type") { hasText("number") }
                    attribute("name") { hasText("decodingDtoList[0].lowerBound") }
                    attribute("value") { hasText("1") }
                }
                node(".decodingUpperBound") {
                    exists()
                    attribute("type") { hasText("number") }
                    attribute("name") { hasText("decodingDtoList[0].upperBound") }
                    attribute("value") { hasText("5") }
                }
                node(".decodingResult") {
                    exists()
                    attribute("name") { hasText("decodingDtoList[0].result") }
                    attribute("value") { hasText("test") }
                }
            }
        }
    }*/
}
