package nsu.fit.qyoga.cases.core.generateLink.ui

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class GenerateLinkViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "db/questionnaires/generate-link-init-script.sql" to "dataSource",
            "db/completing/insert_therapist.sql" to "dataSource",
            "db/migration/demo/V23050904__insert_clients.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-empty-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga return generate link page with list of clients`() {
        Given {
            authorized()
        } When {
            param("questionnaireId", "1")
            get("/therapist/questionnaires/generate-link")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            print(body)
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#searchClientsFilterForm") { exists() }
                node("#nameFilter") { exists() }
                node("#generate-link-content") { exists() }
                node("#client1") {
                    exists()
                    containsText("Иванов Иван Иванович")
                }
                node("#client2") {
                    exists()
                    containsText("Петров Пётр Петрович")
                }
                node("#client3") {
                    exists()
                    containsText("Сергеев Сергей Иванович")
                }
                node("#tablePagination") { exists() }
                node("#questionnaire-url") { exists() }
                node("#close-mw") { exists() }
            }
        }
    }

    @Test
    fun `QYoga return generate link page fragment with list of clients when user set params`() {
        Given {
            authorized()
        } When {
            header("action", "true")
            param("clientName", "п п п")
            param("questionnaireId", "1")
            get("/therapist/questionnaires/generate-link")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#searchClientsFilterForm") { notExists() }
                node("#nameFilter") { notExists() }
                node("#generate-link-content") { exists() }
                node("#client1") { notExists() }
                node("#client2") {
                    exists()
                    containsText("Петров Пётр Петрович")
                }
                node("#client3") { notExists() }
                node("#tablePagination") { exists() }
                node("#questionnaire-url") { notExists() }
                node("#close-mw") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga return generate link page fragment with link to complete when user selects client`() {
        Given {
            authorized()
        } When {
            param("questionnaireId", "1")
            param("clientId", "1")
            post("/therapist/questionnaires/generate-link")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaire-url-text") {
                    exists()
                    attribute("value") {
                        containsText("questionnaireId=1")
                        containsText("clientId=1")
                        containsText("therapistId=1")
                    }
                }
            }
        }
    }

}
