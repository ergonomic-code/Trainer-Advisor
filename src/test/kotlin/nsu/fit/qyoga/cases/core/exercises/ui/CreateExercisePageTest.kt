package nsu.fit.qyoga.cases.core.exercises.ui

import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.platform.lang.toDecimalMinutes
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseStep
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.platform.loadResource
import org.junit.jupiter.api.Test
import java.time.Duration

const val TITLE = "title"
const val INDICATIONS = "indications"
const val CONTRADICTIONS = "contradictions"
const val PURPOSES = "purposes"
const val DURATION = "duration"
const val DESCRIPTION = "description"
const val EXERCISE_TYPE_ID = "exerciseTypeId"

fun stepsDescription(idx: Int) = "steps[$idx].description"

fun stepImage(idx: Int) = "stepImage$idx"

class CreateExercisePageTest : QYogaAppTestBase() {

    @Test
    fun `GET _exercises_create should return exercise creation from`() {
        Given {
            authorized()
        } When {
            get("/therapist/exercises/create")
        } Then {
            assertThatBody {
                node("#createExerciseForm") { exists() }
                node("input[name=$TITLE]") { exists() }
                node("input[name=$INDICATIONS]") { exists() }
                node("input[name=$CONTRADICTIONS]") { exists() }
                node("input[name=$PURPOSES]") { exists() }
                node("input[name=$DURATION]") { exists() }
                node("select[name=$EXERCISE_TYPE_ID]") { exists() }
                node("textarea[name=$DESCRIPTION]") { exists() }
            }
        }
    }

    @Test
    fun `When new exercise is added it should be present in exercises list`() {
        val purpose = "Health a finger"
        val step1 = ExerciseStep("Take a deep breath", null, 1)
        val step2 = ExerciseStep("And exhale", null, 2)
        val exercise = Exercise(
                "Just added exercise",
                "Description",
                "Indications",
                "Contradictions",
                Duration.ofMinutes(2),
                1,
                2,
                setOf(),
                listOf(step1, step2)
        )
        val stepImage = loadResource("/images/testImage.png")
        Given {
            authorized()
            contentType("multipart/form-data; charset=UTF-8")
            filter(RequestLoggingFilter())
            formParam(TITLE, exercise.title)
            formParam(INDICATIONS, exercise.indications)
            formParam(CONTRADICTIONS, exercise.contradictions)
            formParam(PURPOSES, purpose)
            formParam(DURATION, exercise.duration.toDecimalMinutes())
            formParam(EXERCISE_TYPE_ID, exercise.exerciseTypeId)
            formParam(DESCRIPTION, exercise.description)
            formParam(stepsDescription(0), step1.description)
            formParam(stepsDescription(1), step2.description)
            multiPart(stepImage(0), "step1.png", stepImage, "image/png")
            multiPart(stepImage(1), "step2.png", stepImage, "image/png")
        } When {
            post("/therapist/exercises/create")
        } Then {
            header("HX-Redirect", "/therapist/exercises")
        }

        Given {
            authorized()
        } When {
            get("/therapist/exercises")
        } Then {
            assertThatBody {
                hasExercisesTableWithExercise(exercise, "Разминка", purpose)
            }
        }
    }

}
