package nsu.fit.qyoga.cases.app.external

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.fixture.adminToken
import nsu.fit.qyoga.infra.fixture.brokenAdminToken
import nsu.fit.qyoga.infra.fixture.editedAdminToken
import nsu.fit.qyoga.infra.fixture.token
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test


class AuthorizationTest : QYogaAppTestBase() {

    @Test
    fun `QYoga should allow access to admin's endpoints with valid admin token`() {
        Given {
            token(adminToken)
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
            token(editedAdminToken)
        } When {
            post("users")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

    @Test
    fun `QYoga should prohibit access to restricted endpoint with broken token`() {
        Given {
            token(brokenAdminToken)
        } When {
            post("users")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

}