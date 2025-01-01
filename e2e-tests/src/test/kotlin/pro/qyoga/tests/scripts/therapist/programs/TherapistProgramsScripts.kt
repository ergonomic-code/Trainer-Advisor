package pro.qyoga.tests.scripts.therapist.programs

import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$$`
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.tests.pages.therapist.therapy.programs.EditProgramForm
import pro.qyoga.tests.pages.therapist.therapy.programs.EditProgramPage
import pro.qyoga.tests.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.platform.selenide.await
import pro.qyoga.tests.platform.selenide.click
import pro.qyoga.tests.platform.selenide.typeInto

fun goToEditProgramPage(programId: Long) {
    Selenide.open(ProgramsListPage.editProgramPath(programId))
}

fun editProgramTo(program: Program) {
    typeInto(EditProgramForm.titleInput, program.title)
    typeInto(EditProgramForm.therapeuticTaskInput, program.therapeuticTaskRef.resolveOrThrow().name)

    editProgramExercises(program)

    click(EditProgramForm.save)
    await(ProgramsListPage)
}

private fun editProgramExercises(program: Program) {
    removeMissingExercises(program)
    addNewExercises(program)
}

private fun removeMissingExercises(program: Program) {
    val exercises = `$$`(EditProgramPage.exercisesSelector)
    val exercisesToKeepIds = program.exercises.map { it.exerciseRef.id.toString() }.toSet()
    exercises.filter { it.find(".id").`val`() !in exercisesToKeepIds }
        .forEach { it.click(EditProgramForm.removeButtonSelector); }
}

private fun addNewExercises(program: Program) {
    val currentExercisesIds = `$$`(EditProgramPage.exercisesSelector)
        .map { it.find(".id").`val`()!!.toLong() }

    program.exercises.filter { it.exerciseRef.id !in currentExercisesIds }
        .forEach { addExercise(it.exerciseRef.resolveOrThrow()) }
}

fun addExercise(exercise: Exercise) {
    typeInto(EditProgramForm.exerciseSearchInput, exercise.title)
    `$$`(".found-exercise").findBy(text(exercise.title)).click()
}
