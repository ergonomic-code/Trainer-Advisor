package nsu.fit.qyoga.cases.core.exercises.external

import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

class ExercisesTest : QYogaAppTestBase() {

    @Test
    fun `QYoga should return list of exercise types`() {
        var types = listOf<String>()
        When {
            get("/exercises/types")
        } Then {
            statusCode(HttpStatus.SC_OK)
            types = this.extract().jsonPath().getList("$")
        }
        types.size shouldBe 6
        types shouldBe listOf(
            "ResistanceBreathing",
            "Balance",
            "Stretching",
            "WeightBearingExercise",
            "StrengthTraining",
            "CardioTraining"
        )
    }
}
