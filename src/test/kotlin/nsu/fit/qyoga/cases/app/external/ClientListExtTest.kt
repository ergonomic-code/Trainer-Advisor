package nsu.fit.qyoga.cases.app.external

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.hamcrest.core.StringEndsWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

private const val USERNAME_FORM_PARAM = "username"
private const val PASSWORD_FORM_PARAM = "password"

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
        val cookie = Given {
            formParam(USERNAME_FORM_PARAM, "therapist")
            formParam(PASSWORD_FORM_PARAM, "diem-Synergy5")
        }.post("/users/login").thenReturn().detailedCookie("JSESSIONID")
        Given {
            this.cookie(cookie)
        } When {
            post("/clients/delete/1")
        } Then {
            statusCode(302)
            header("Location", StringEndsWith.endsWith("clients"))
        }
    }

    @Test
    fun `When user delete not valid client he should be redirected to client page`() {
        val cookie = Given {
            formParam(USERNAME_FORM_PARAM, "therapist")
            formParam(PASSWORD_FORM_PARAM, "diem-Synergy5")
        }.post("/users/login").thenReturn().detailedCookie("JSESSIONID")
        Given {
            this.cookie(cookie)
        } When {
            post("/clients/delete/1111")
        } Then {
            statusCode(302)
            header("Location", StringEndsWith.endsWith("clients"))
        }
    }
}
