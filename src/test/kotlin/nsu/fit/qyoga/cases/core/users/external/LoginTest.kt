package nsu.fit.qyoga.cases.core.users.external

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.fixture.adminLoginRequest
import nsu.fit.qyoga.infra.fixture.adminWithInvalidPasswordLoginRequest
import nsu.fit.qyoga.infra.fixture.notExistingUserLoginRequest
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

class LoginTest : QYogaAppTestBase() {

    @Test
    fun `QYoga should allow users to login`() {
        Given {
            body(adminLoginRequest)
        } When {
            post("users/login")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body(Matchers.matchesRegex(".+\\..+\\..+"))
        }
    }

    @Test
    fun `QYoga should return 401 code on login with not existing username`() {
        Given {
            body(notExistingUserLoginRequest)
        } When {
            post("users/login")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

    @Test
    fun `QYoga should return 401 code on login with not invalid password`() {
        Given {
            body(adminWithInvalidPasswordLoginRequest)
        } When {
            post("users/login")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

}