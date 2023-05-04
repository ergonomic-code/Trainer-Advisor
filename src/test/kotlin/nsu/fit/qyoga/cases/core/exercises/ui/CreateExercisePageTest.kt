package nsu.fit.qyoga.cases.core.exercises.ui

import io.github.ulfs.assertj.jsoup.Assertions
import io.github.ulfs.assertj.jsoup.DocumentAssertionsSpec
import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test


const val inputName = "inputName"

class CreateExercisePageTest : QYogaAppTestBase() {

    @Test
    fun `GET _exercises_create should return exercise creation from`() {
        Given {
            authenticatedWith(getAuthCookie())
        } When {
            get("/exercises/create")
        } Then {
            assertThatBody {
                node("#createExerciseForm") { exists() }
                node("#$inputName") { exists() }
            }
        }
    }

    @Test
    fun `When new exercise is added it should be present in exercises list`() {
        Given {
            authenticatedWith(getAuthCookie())
            formParam(inputName, "Just added exercise")
        } When {
            post("/exercises/create")
        } Then {
            assertThatBody {
                node("#createExerciseForm") { exists() }
            }
        }
    }

    private fun ValidatableResponse.assertThatBody(body: DocumentAssertionsSpec.() -> DocumentAssertionsSpec) {
        val document = Jsoup.parse(extract().body().asString())
        Assertions.assertThatSpec(document, assert = body)
    }

    private fun RequestSpecification.authenticatedWith(authCookie: Cookie): RequestSpecification =
        this.cookie(authCookie)

}