package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import pro.qyoga.core.therapy.exercises.api.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.ExerciseDto
import pro.qyoga.platform.java.time.toDecimalMinutes


infix fun ExerciseDto.shouldMatch(source: CreateExerciseRequest) {
    title shouldBe source.title
    description shouldBe source.description
    duration.toDecimalMinutes() shouldBe source.duration
    type shouldBe source.exerciseType
}