package nsu.fit.qyoga.cases.core.exercises.ui

import io.github.ulfs.assertj.jsoup.DocumentAssertionsSpec
import nsu.fit.platform.lang.toDecimalMinutes
import nsu.fit.qyoga.core.exercises.api.model.Exercise

fun DocumentAssertionsSpec.hasExercisesTableWithExercise(
    exercise: Exercise,
    exerciseTypeName: String,
    exercisePurpose: String
): DocumentAssertionsSpec {
    node("td:nth-child(1)") {
        hasText(exercise.title)
    }
    node("td:nth-child(2)") {
        hasText(exercise.contradictions)
    }
    node("td:nth-child(3)") {
        hasText("${exercise.duration.toDecimalMinutes()} мин.")
    }
    node("td:nth-child(4)") {
        hasText(exerciseTypeName)
    }
    node("td:nth-child(5)") {
        hasText(exercisePurpose)
    }
    return this
}
