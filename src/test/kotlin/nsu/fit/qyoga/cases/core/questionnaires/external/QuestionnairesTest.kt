package nsu.fit.qyoga.cases.core.questionnaires.external

import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

class QuestionnairesTest : QYogaAppTestBase() {

    @Test
    fun `QYoga should return list of exercise types`() {
        var types = listOf<String>()
        When {
            get("/questionnaires")
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