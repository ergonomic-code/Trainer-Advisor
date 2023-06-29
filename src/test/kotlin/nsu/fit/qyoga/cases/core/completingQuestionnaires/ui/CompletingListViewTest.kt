package nsu.fit.qyoga.cases.core.completingQuestionnaires.ui

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
                node("#table-body") { exists() }
                node("#completing1") { exists() }
                node("#completing2") { exists() }
                node("#completing3") { exists() }
                node("#completing4") { exists() }
                node("#completing5") { exists() }
                node("#completing6") { exists() }
                node("#completing7") { exists() }
                node("#completing8") { exists() }
                node("#completing10") { exists() }
                node("#completing12") { exists() }
                node(".table-completing-row") { exists() }
                node("#sort-type-select") { exists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns table of completing when user change page`() {
        Given {
            authorized()
            header("action", "true")
            param("page", 1)
        } When {
            get("/therapist/questionnaires/performing")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") { exists() }
                node("#completing13") { exists() }
                node(".table-completing-row") { exists() }
                node("#sort-type-select") { exists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns table of completing sorted by date DESC when user change sort type`() {
        Given {
            authorized()
            header("action", "true")
            param("sort", "date,desc")
        } When {
            get("/therapist/questionnaires/performing")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") { exists() }
                node("#completing13") { exists() }
                node("#completing12") { exists() }
                node("#completing10") { exists() }
                node("#completing8") { exists() }
                node("#completing7") { exists() }
                node("#completing6") { exists() }
                node("#completing5") { exists() }
                node("#completing4") { exists() }
                node("#completing3") { exists() }
                node("#completing2") { exists() }
                node(".table-completing-row") { exists() }
                node("#sort-type-select") { exists() }
                node("#client-find-bar") { exists() }
                node("#questionnaire-find-bar") { exists() }
                node("#completing-navigation-bar") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns table of completing where title in completing title, when user change title`() {
        Given {
            authorized()
            header("action", "true")
            param("title", "test")
        } When {
            get("/therapist/questionnaires/performing")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") { exists() }
                node("#completing2Title") {
                    exists()
                    containsText("test")
                }
                node("#completing4Title") {
                    exists()
                    containsText("test")
                }
                node("#completing6Title") {
                    exists()
                    containsText("test")
                }
                node("#completing8Title") {
                    exists()
                    containsText("test")
                }
                node("#completing13Title") {
                    exists()
                    containsText("test")
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
    fun `QYoga returns table of completing where name in client full name, when user change client name`() {
        Given {
            authorized()
            header("action", "true")
            param("clientName", "first_name2")
        } When {
            get("/therapist/questionnaires/performing")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#table-body") { exists() }
                node("#completing6ClientName") {
                    exists()
                    containsText("first_name2")
                }
                node("#completing8ClientName") {
                    exists()
                    containsText("first_name2")
                }
                node("#completing10ClientName") {
                    exists()
                    containsText("first_name2")
                }
                node("#completing12ClientName") {
                    exists()
                    containsText("first_name2")
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
