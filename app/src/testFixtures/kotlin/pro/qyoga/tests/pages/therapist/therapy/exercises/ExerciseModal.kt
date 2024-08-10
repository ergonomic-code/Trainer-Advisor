package pro.qyoga.tests.pages.therapist.therapy.exercises

import io.kotest.matchers.collections.shouldBeSameSizeAs
import pro.qyoga.app.therapist.therapy.exercises.compenents.ExerciseModalController
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.platform.html.div
import pro.qyoga.tests.platform.html.h3


object ExerciseModal {

    const val PATH = ExerciseModalController.PATH

    fun selector(): String = "#exercise-modal"

    fun forExercise(exercise: Exercise): PageMatcher = PageMatcher { element ->
        element shouldHaveComponent h3("exercise-title", exercise.title)
        element shouldHaveComponent div("exercise-type", text = exercise.exerciseType.label)
        element shouldHaveComponent div("exercise-description", text = exercise.description)

        val stepElements = element.getElementsByClass("step-row")
        stepElements shouldBeSameSizeAs exercise.steps
        stepElements.zip(exercise.steps).forEachIndexed { idx, (stepElement, step) ->
            stepElement shouldHaveComponent div(
                clazz = "step-description",
                text = "Шаг ${idx + 1}: ${step.description}"
            )
        }
    }

}