package nsu.fit.qyoga.cases.core.completingQuestionnaires.ui

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CompletingListViewTest : QYogaAppTestBase() {

    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/completing/completing-questionnaires-init-script.sql" to "dataSource",
            "/db/migration/common/V23051304__insert_questionnaires_data.sql" to "dataSource",
            "/db/completing/insert_therapist.sql" to "dataSource",
            "/db/completing/insert_completing_data.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga returns completing-list page with questionnaires`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/performing")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") {
                    exists()
                    containsText("01-05-2023")
                    containsText("02-05-2023")
                    containsText("03-05-2023")
                    containsText("04-05-2023")
                    containsText("05-05-2023")
                    containsText("06-05-2023")
                    containsText("07-05-2023")
                    containsText("08-05-2023")
                    containsText("10-05-2023")
                    containsText("12-05-2023")
                }
                node(".table-completing-row") { exists() }
                node("#sort-type-select") { exists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page completing-list when user change sort type`() {
        Given {
            authorized()
            contentType(ContentType.JSON)
            param("sort", "date,desc")
        } When {
            get("/therapist/questionnaires/performing/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") {
                    exists()
                    containsText("13-05-2023")
                    containsText("12-05-2023")
                    containsText("10-05-2023")
                    containsText("8-05-2023")
                    containsText("7-05-2023")
                    containsText("6-05-2023")
                    containsText("5-05-2023")
                    containsText("4-05-2023")
                    containsText("2-05-2023")
                    containsText("2-05-2023")
                }
                node(".table-completing-row") { exists() }
                node("#sort-type-select") { exists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page completing-list when user change title`() {
        Given {
            authorized()
            contentType(ContentType.JSON)
            param("title", "test")
        } When {
            get("/therapist/questionnaires/performing/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") {
                    exists()
                    containsText("02-05-2023")
                    containsText("04-05-2023")
                    containsText("06-05-2023")
                    containsText("08-05-2023")
                    containsText("13-05-2023")
                }
                node(".table-completing-row") { exists() }
                node("#sort-type-select") { exists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page completing-list when user change client name`() {
        Given {
            authorized()
            contentType(ContentType.JSON)
            param("clientName", "first_name2")
        } When {
            get("/therapist/questionnaires/performing/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") {
                    exists()
                    containsText("06-05-2023")
                    containsText("08-05-2023")
                    containsText("10-05-2023")
                    containsText("12-05-2023")
                }
                node(".table-completing-row") { exists() }
                node("#sort-type-select") { exists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns part of page completing-list when user change page`() {
        Given {
            authorized()
            contentType(ContentType.JSON)
            param("page", 1)
        } When {
            get("/therapist/questionnaires/performing/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") {
                    exists()
                    containsText("13-05-2023")
                }
                node(".table-completing-row") { exists() }
                node("#sort-type-select") { exists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }
}
