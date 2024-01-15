package pro.qyoga.tests.assertions

import io.kotest.assertions.withClue
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import pro.qyoga.core.therapy.exercises.api.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSummaryDto
import pro.qyoga.platform.java.time.toDecimalMinutes


infix fun ExerciseSummaryDto.shouldMatch(source: CreateExerciseRequest) {
    title shouldBe source.summary.title
    description shouldBe source.summary.description
    duration.toDecimalMinutes() shouldBe source.summary.duration
    type shouldBe source.summary.type
}

infix fun ExerciseSummaryDto?.shouldMatch(another: ExerciseSummaryDto) {
    withClue("Expected not null value but got null") {
        this shouldNotBe null
    }

    this!!.shouldBeEqualToIgnoringFields(another, ExerciseSummaryDto::id)
}