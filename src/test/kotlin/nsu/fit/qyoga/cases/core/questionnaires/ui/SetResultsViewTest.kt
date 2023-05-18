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

class SetResultsViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource",
        )
    }

    @Test
    fun `QYoga returns empty questionnaire-decoding page`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/setResult")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("table") {
                    exists()
                    containsHtml("id=\"addDecodingBtn\"")
                    containsHtml("id=\"tableBody\"")
                }
                node("#addDecodingBtn") {
                    containsText("Нажмите для добавления строки")
                    attribute("hx-get") { hasText("/therapist/questionnaires/setResult/addResult") }
                }
                node("#saveBtn") {
                    containsText("Сохранить")
                    attribute("type") { hasText("submit") }
                }
                node("decodingRow") {
                    notExists()
                }
            }
        }
    }

    @Test
    fun `QYoga can't get result list page if questionnaire not exists in session`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/edit/setResult")
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
    fun `QYoga can add new decoding entry`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/setResult/addResult")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#decoding0Id") {
                    exists()
                    attribute("type") { hasText("hidden") }
                    attribute("name") { hasText("decoding[0].id") }
                }
                node("button") {
                    exists()
                    attribute("class") { hasText("field") }
                    attribute("hx-delete") { hasText("/therapist/questionnaires/setResult/0") }
                    hasText("Удалить")
                }
                node("#decoding0LowerBound") {
                    exists()
                    attribute("type") { hasText("number") }
                    attribute("name") { hasText("decoding[0].lowerBound") }
                }
                node("#decoding0UpperBound") {
                    exists()
                    attribute("type") { hasText("number") }
                    attribute("name") { hasText("decoding[0].upperBound") }

                }
                node("#decoding0Result") {
                    exists()
                    attribute("name") { hasText("decoding[0].result") }
                }
            }
        }
    }

    @Test
    fun `QYoga will add decoding with next id on add`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/setResult/addResult")
            get("/therapist/questionnaires/setResult/addResult")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#decoding1Id") {
                    exists()
                    attribute("type") { hasText("hidden") }
                    attribute("name") { hasText("decoding[1].id") }
                }
                node("#decoding1LowerBound") {
                    exists()
                    attribute("type") { hasText("number") }
                    attribute("name") { hasText("decoding[1].lowerBound") }
                }
                node("#decoding1UpperBound") {
                    exists()
                    attribute("type") { hasText("number") }
                    attribute("name") { hasText("decoding[1].upperBound") }

                }
                node("#decoding1Result") {
                    exists()
                    attribute("name") { hasText("decoding[1].result") }
                }
            }
        }
    }

    @Test
    fun `QYoga can't add new result if questionnaire not exists in session`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/edit/setResult")
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
    fun `QYoga can delete result entry`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/setResult/addResult")
            get("/therapist/questionnaires/setResult/addResult")
            delete("/therapist/questionnaires/setResult/0")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#decoding0Id") {
                    exists()
                    attribute("type") { hasText("hidden") }
                    attribute("name") { hasText("decoding[0].id") }
                }
                node("button") {
                    exists()
                    attribute("class") { hasText("field") }
                    attribute("hx-delete") { hasText("/therapist/questionnaires/setResult/1") }
                    hasText("Удалить")
                }
                node("#decoding0LowerBound") {
                    exists()
                    attribute("type") { hasText("number") }
                    attribute("name") { hasText("decoding[0].lowerBound") }
                }
                node("#decoding0UpperBound") {
                    exists()
                    attribute("type") { hasText("number") }
                    attribute("name") { hasText("decoding[0].upperBound") }

                }
                node("#decoding0Result") {
                    exists()
                    attribute("name") { hasText("decoding[0].result") }
                }
            }
        }
    }

    @Test
    fun `QYoga can't delete result if questionnaire not exists in session`() {
        Given {
            authorized()
        } When {
            delete("/therapist/questionnaires/setResult/0")
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
    fun `QYoga can save changes`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/setResult/addResult")
            get("/therapist/questionnaires/setResult/addResult")
            params(setParams())
            post("/therapist/questionnaires/setResult/0/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") { notExists() }
                node(".error-text") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga can't save changes if result is not exists`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/setResult/-1/update")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") {
                    exists()
                    hasText("Перезагрузить")
                }
                node(".error-text") {
                    exists()
                    hasText("Выбранная расшифровка результатов не найдена Перезагрузить")
                }
            }
        }
    }

    @Test
    fun `QYoga can't update questionnaire if it not in session`() {
        Given {
            authorized()
        } When {
            params(setParams())
            post("/therapist/questionnaires/setResult/0/update")
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
    fun `QYoga redirect on questionnaire list page after save`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/setResult")
        } Then {
            extract().statusCode().compareTo(302) shouldBe 0
        }
    }

    @Test
    fun `QYoga can't save questionnaire if it not in session`() {
        Given {
            authorized()
        } When {
            params(setParams())
            post("/therapist/questionnaires/edit/setResult")
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

    fun setParams(): Map<String, String> {
        return mutableMapOf(
            "id" to "1",
            "title" to "test",
            "question[0].id" to "0",
            "question[0].title" to "questions title",
            "question[0].questionType" to "SEVERAL",
            "decoding[0].id" to "0",
            "decoding[0].lowerBound" to "1",
            "decoding[0].upperBound" to "10",
            "decoding[0].result" to "result"
        )
    }
}
