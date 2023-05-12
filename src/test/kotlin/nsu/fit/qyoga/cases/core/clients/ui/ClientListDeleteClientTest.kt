package nsu.fit.qyoga.cases.core.clients.ui

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ClientListDeleteClientTest : QYogaAppTestBase() {

    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/clients-init-script.sql" to "dataSource",
            "/db/migration/demo/V23050904__insert_clients.sql" to "dataSource",
        )
    }

    @Test
    fun `When user delete valid client he should be redirected to client page`() {
        Given {
            authorized()
        } When {
            delete("${DELETE_CLIENT_ENDPOINT_BASE}1")
        } Then {
            statusCode(200)
        }
    }

    @Test
    fun `When user delete not valid client he should be redirected to client page`() {
        Given {
            authorized()
        } When {
            delete("/therapist/clients/delete/1111")
        } Then {
            statusCode(200)
        }
    }
}
