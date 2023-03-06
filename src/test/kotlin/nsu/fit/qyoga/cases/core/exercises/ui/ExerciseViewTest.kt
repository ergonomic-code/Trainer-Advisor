package nsu.fit.qyoga.cases.core.exercises.ui

import io.github.ulfs.assertj.jsoup.Assertions
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

class ExerciseViewTest : QYogaAppTestBase() {

    @Test
    fun `QYoga returns exercise-search page with exercise table`() {
        When {
            get("/exercises/")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#exercisesTable") { exists() }
                node("#exerciseRow") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns exercise-search page with input fields`() {
        When {
            get("/exercises/")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#titleFilter") { exists() }
                node("#contradictionFilter") { exists() }
                node("#therapeuticPurposeFilter") { exists() }
                node("#exerciseTypeFilter") { exists() }

            }
        }
    }
}
