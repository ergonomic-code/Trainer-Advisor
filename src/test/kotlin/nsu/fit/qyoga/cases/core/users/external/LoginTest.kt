package nsu.fit.qyoga.cases.core.users.external

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.fixture.adminWithInvalidPasswordLoginRequest
import nsu.fit.qyoga.infra.fixture.notExistingUserLoginRequest
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

class LoginTest : QYogaAppTestBase() {
    @Test
    fun `QYoga should allow users to login`() {
        Given {
            param("username", "admin")
            param("password", "diem-Synergy5")
        } When {
            post("/users/login")
        } Then {
            statusCode(HttpStatus.SC_OK)
        }
    }

    @Test
    fun `QYoga should return 401 code on login with not existing username`() {
        Given {
            body(notExistingUserLoginRequest)
        } When {
            post("/users/login")
        } Then {
            statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
        }
    }

    @Test
    fun `QYoga should return 401 code on login with not invalid password`() {
        Given {
            body(adminWithInvalidPasswordLoginRequest)
        } When {
            post("users/login")
        } Then {
            statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
        }
    }

}
