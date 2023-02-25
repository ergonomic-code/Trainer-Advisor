package nsu.fit.qyoga.cases.core.exercises.external

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

class ExercisesTest : QYogaAppTestBase() {

    @Test
    fun `QYoga should return list of exercise type`() {
        When {
            post("users/login")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body(Matchers.matchesRegex(".+\\..+\\..+"))
        }
    }
}
