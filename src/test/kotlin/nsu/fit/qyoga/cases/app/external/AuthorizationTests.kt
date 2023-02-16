package nsu.fit.qyoga.cases.app.external

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.fixture.ADMIN_TOKEN
import nsu.fit.qyoga.infra.fixture.BROKEN_ADMIN_TOKEN
import nsu.fit.qyoga.infra.fixture.EDITED_ADMIN_TOKEN
import nsu.fit.qyoga.infra.fixture.token
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

class AuthorizationTests : QYogaAppTestBase() {

    @Test
    fun `QYoga should allow access to admin's endpoints with valid admin token`() {
        Given {
            token(ADMIN_TOKEN)
        } When {
            post("users")
        } Then {
            statusCode(HttpStatus.SC_OK)
        }
    }

    @Test
    fun `QYoga should prohibit access to restricted endpoint without auth header`() {
        Given {
            this
        } When {
            post("users")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

    @Test
    fun `QYoga should prohibit access to restricted endpoint with edited token`() {
        Given {
            token(EDITED_ADMIN_TOKEN)
        } When {
            post("users")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

    @Test
    fun `QYoga should prohibit access to restricted endpoint with broken token`() {
        Given {
            token(BROKEN_ADMIN_TOKEN)
        } When {
            post("users")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

}
