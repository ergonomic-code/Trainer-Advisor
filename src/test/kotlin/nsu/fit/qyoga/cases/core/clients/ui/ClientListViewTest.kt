package nsu.fit.qyoga.cases.core.clients.ui

import io.github.ulfs.assertj.jsoup.Assertions
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ClientListViewTest : QYogaAppTestBase() {

    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/clients-init-script.sql" to "dataSource",
            "/db/insert-clients.sql" to "dataSource",
        )
    }

    @Test
    fun `QYoga returns client-search page with clients table`() {
        When {
            get("/clients")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#clientsTable") { exists() }
                node("#clients-list") { exists() }
                node("td") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns clients-search page with input fields`() {
        When {
            get("/clients")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#lastnameFilter") { exists() }
                node("#firstnameFilter") { exists() }
                node("#patronymicFilter") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns clients table with pagination`() {
        When {
            get("/clients/search-cl")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#clientsTable") { exists() }
                node("#clients-list") { exists() }
                node("td") { exists() }
                node("#pagination") { exists() }
            }
        }
    }

    fun `QYoga returns clients table with deleted client`() {
        When {
            post("/clients/delete/1")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#clientsTable") { exists() }
                node("#clients-list") { exists() }
                node("td") { exists() }
            }
        }
    }
}
