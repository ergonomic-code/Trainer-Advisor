package nsu.fit.qyoga.cases.core.exercises.ui

import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.platform.lang.toDecimalMinutes
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.junit.jupiter.api.Test
import java.time.Duration

const val TITLE = "title"
const val INDICATIONS = "indications"
const val CONTRADICTIONS = "contradictions"
const val PURPOSES = "purposes"
const val DURATION = "duration"
const val DESCRIPTION = "description"
const val EXERCISETYPEID = "exerciseTypeId"

class CreateExercisePageTest : QYogaAppTestBase() {

    @Test
    fun `GET _exercises_create should return exercise creation from`() {
        Given {
            cookie(getAuthCookie())
        } When {
            get("/exercises/create")
        } Then {
            assertThatBody {
                node("#createExerciseForm") { exists() }
                node("input[name=$TITLE]") { exists() }
                node("input[name=$INDICATIONS]") { exists() }
                node("input[name=$CONTRADICTIONS]") { exists() }
                node("input[name=$PURPOSES]") { exists() }
                node("input[name=$DURATION]") { exists() }
                node("select[name=$EXERCISETYPEID]") { exists() }
                node("textarea[name=$DESCRIPTION]") { exists() }
            }
        }
    }

    @Test
    fun `When new exercise is added it should be present in exercises list`() {
        val purpose = "Вылечить пальчик"
        val exercise = Exercise(
            "Just added exercise",
            "Description",
            "Indications",
            "Contradictions",
            Duration.ofMinutes(2),
            1,
            2,
            setOf(),
            0
        )
        Given {
            cookie(getAuthCookie())
            filter(RequestLoggingFilter())
            formParam(TITLE, exercise.title)
            formParam(INDICATIONS, exercise.indications)
            formParam(CONTRADICTIONS, exercise.contradictions)
            formParam(PURPOSES, purpose)
            formParam(DURATION, exercise.duration.toDecimalMinutes())
            formParam(EXERCISETYPEID, exercise.exerciseTypeId)
            formParam(DESCRIPTION, exercise.description)
        } When {
            post("/exercises/create")
        } Then {
            header("HX-Redirect", "/exercises")
        }

        Given {
            this
        } When {
            get("/exercises")
        } Then {
            assertThatBody {
                hasExercisesTableWithExercise(exercise, "Разминка", "Вылечить пальчик")
            }
        }
    }

}
