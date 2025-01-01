package pro.qyoga.tests.assertions.programs.edit

import com.codeborne.selenide.Selenide.`$$`
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.tests.pages.therapist.therapy.programs.EditProgramForm
import pro.qyoga.tests.pages.therapist.therapy.programs.EditProgramPage
import pro.qyoga.tests.platform.selenide.find


fun currentPageShouldMatch(program: Program) {
    find(EditProgramForm.titleInput).`val`() shouldBe program.title
    find(EditProgramForm.therapeuticTaskInput).`val`() shouldBe program.therapeuticTaskRef.resolveOrThrow().name
    val exercises = `$$`(EditProgramPage.exercisesSelector)
    exercises.size() shouldBe program.exercises.size

    exercises.zip(program.exercises.map { it.exerciseRef.resolveOrThrow() })
        .forEach { (pageExercise, expectedExercise) ->
            pageExercise.find(".title").text() shouldBe expectedExercise.title
            val summary = pageExercise.find(".summary")
            summary.text() shouldContain expectedExercise.exerciseType.label
            summary.text() shouldContain expectedExercise.duration.toMinutes().toString()
        }
}
