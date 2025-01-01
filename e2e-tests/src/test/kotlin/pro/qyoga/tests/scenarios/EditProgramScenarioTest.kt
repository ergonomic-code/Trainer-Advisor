package pro.qyoga.tests.scenarios

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.programs.model.ProgramExercise
import pro.qyoga.tests.assertions.programs.edit.currentPageShouldMatch
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.infra.QYogaE2EBaseTest
import pro.qyoga.tests.scripts.loginAsTheTherapist
import pro.qyoga.tests.scripts.therapist.programs.editProgramTo
import pro.qyoga.tests.scripts.therapist.programs.goToEditProgramPage


@DisplayName("Редактирование программы")
class EditProgramScenarioTest : QYogaE2EBaseTest() {

    @Test
    fun `успешное редактирование`() {
        // Фикстура
        val program = backgrounds.programs.createRandomProgram(exercisesCount = 3)
        val newTherapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val newTitle = randomCyrillicWord()
        val addedExercise = backgrounds.exercises.createExercise()
        val removedExercise = program.exercises[1].exerciseRef.resolveOrThrow()
        val editedProgram = program.copy(
            title = newTitle,
            therapeuticTaskRef = newTherapeuticTask.ref(),
            exercises = program.exercises.filter { it.exerciseRef.id != removedExercise.id } + ProgramExercise(
                addedExercise.ref()
            )
        )

        loginAsTheTherapist()

        // Терапевт переходит на страницу редактирования программы
        goToEditProgramPage(program.id)

        // И видит корректно заполненную форму программы
        currentPageShouldMatch(program)

        // Редактирует программу и открывает её заново
        editProgramTo(editedProgram)
        goToEditProgramPage(program.id)

        // И видит форму программы, заполненную обновлёнными данными
        currentPageShouldMatch(editedProgram)
    }

}