package pro.qyoga.tests.assertions

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.platform.spring.sdj.erpo.hydration.resolveOrThrow


infix fun Program.shouldMatch(request: Pair<CreateProgramRequest, String>) {
    this.title shouldBe request.first.title
    this.therapeuticTaskRef.resolveOrThrow().name shouldBe request.second
    this.exercises shouldHaveSize request.first.exerciseIds.size
    (this.exercises zip request.first.exerciseIds).forAll { (programExercise, requestExerciseId) ->
        programExercise.exerciseRef.id shouldBe requestExerciseId
    }
}
