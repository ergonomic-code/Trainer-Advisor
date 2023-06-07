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

class PageableUiTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/completing/completing-questionnaires-init-script.sql" to "dataSource",
            "/db/completing/insert_therapist.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga returns empty page completing-list if nothing exists`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/performing")
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
