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

class SetDecodingViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga returns empty questionnaire-decoding page`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/questionnaires/1/setResult")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("table"){
                    exists()
                    containsHtml("id=\"addDecodingBtn\"")
                    containsHtml("id=\"tableBody\"")
                }
                node("#addDecodingBtn"){
                    containsText("Нажмите для добавления строки")
                    attribute("hx-get") { hasText("/questionnaires/1/setResults/addResult") }
                }
                node("#saveBtn"){
                    containsText("Сохранить")
                    attribute("type"){ hasText("submit") }
                }
                node("decodingRow"){
                    notExists()
                }
            }
        }
    }

    @Test
    fun `QYoga can add new decoding entry`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/questionnaires/1/setResult/addResult")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("tr"){ exists() }
                node("input"){
                    exists()
                    attribute("type"){ hasText("hidden") }
                    attribute("name"){ hasText("decodingDtoList[0].id") }
                }
                node("input"){
                    exists()
                    attribute("type"){ hasText("hidden") }
                    attribute("name"){ hasText("decodingDtoList[0].questionnaireId") }
                }
                node("input"){
                    exists()
                    attribute("type"){ hasText("number") }
                    attribute("name"){ hasText("decodingDtoList[0].lowerBound") }
                }
                node("input"){
                    exists()
                    attribute("type"){ hasText("number") }
                    attribute("name"){ hasText("decodingDtoList[0].upperBound") }
                }
                node("input"){
                    exists()
                    attribute("type"){ hasText("text") }
                    attribute("name"){ hasText("decodingDtoList[0].result") }
                }
            }
        }
    }

    @Test
    fun `QYoga can delete decoding entry`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/questionnaires/1/setResult/addResult")
            delete("/questionnaires/1/setResult/addResult/1")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("table"){
                    exists()
                    containsHtml("id=\"addDecodingBtn\"")
                    containsHtml("id=\"tableBody\"")
                }
                node("#addDecodingBtn"){
                    containsText("Нажмите для добавления строки")
                    attribute("hx-get") { hasText("/questionnaires/1/setResults/addResult") }
                }
                node("#saveBtn"){
                    containsText("Сохранить")
                    attribute("type"){ hasText("submit") }
                }
                node("test"){
                    notExists()
                }
            }
        }
    }

    @Test
    fun `QYoga can save changes`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/questionnaires/1/setResult/addResult")
            param("decodingDtoList[0].id", "1")
            param("decodingDtoList[0].questionnaireId", "1")
            param("decodingDtoList[0].lowerBound", "1")
            param("decodingDtoList[0].upperBound", "5")
            param("decodingDtoList[0].result", "test")
            post("/questionnaires/setResults/1/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga redirect on questionnaire list page after save`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            post("/questionnaires/setResult")
        } Then {
            extract().statusCode().compareTo(302) shouldBe 0
            //extract().header("Location").compareTo("http://localhost:8080/questionnaires/")
        }
    }
}