package nsu.fit.qyoga.cases.app.external

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ClientListExtTest : QYogaAppTestBase() {

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
    fun `When user delete valid client he should be redirected to client page`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            delete("/clients/delete/1")
        } Then {
            statusCode(200)
        }
    }

    @Test
    fun `When user delete not valid client he should be redirected to client page`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            delete("/clients/delete/1111")
        } Then {
            statusCode(200)
        }
    }
}
