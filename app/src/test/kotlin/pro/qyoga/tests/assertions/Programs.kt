package pro.qyoga.tests.assertions

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.model.DocxExercise
import pro.qyoga.core.therapy.programs.model.DocxProgram
import pro.qyoga.core.therapy.programs.model.Program


infix fun Program.shouldMatch(request: Pair<CreateProgramRequest, String>) {
    this.title shouldBe request.first.title
    this.therapeuticTaskRef.resolveOrThrow().name shouldBe request.second
    this.exercises shouldHaveSize request.first.exerciseIds.size
    (this.exercises zip request.first.exerciseIds).forAll { (programExercise, requestExerciseId) ->
        programExercise.exerciseRef.id shouldBe requestExerciseId
    }
}

infix fun DocxProgram?.shouldMatch(program: Program) {
    this shouldNotBe null
    this!!.title shouldBe program.title
    this.exercises shouldHaveSize program.exercises.size
    this.exercises.zip(program.exercises).forAll { (docxEx, ex) ->
        docxEx shouldMatch ex.exerciseRef.resolveOrThrow()
    }
}

infix fun DocxExercise.shouldMatch(exercise: Exercise) {
    this.title shouldBe exercise.title
    this.steps shouldHaveSize exercise.steps.size
    this.steps.zip(exercise.steps).forAll { (docxStep, step) ->
        docxStep shouldBe step
    }
}