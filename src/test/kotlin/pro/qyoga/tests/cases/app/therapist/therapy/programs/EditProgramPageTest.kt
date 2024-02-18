package pro.qyoga.tests.cases.app.therapist.therapy.programs

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.therapy.programs.CreateProgramForm
import pro.qyoga.tests.pages.therapist.therapy.programs.EditProgramPage
import pro.qyoga.tests.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapy.programs.ProgramsObjectMother.randomCreateProgramRequest
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class EditProgramPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Program edit page should be rendered correctly`() {
        // Given
        val program = backgrounds.programs.createRandomProgram(exercisesCount = 3)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.getProgramEditPage(program.id)

        // Then
        document shouldBePage EditProgramPage.pageFor(program)
    }

    @Test
    fun `Program edition should be persistent`() {
        // Given
        val program = backgrounds.programs.createRandomProgram(exercisesCount = 3)

        val newTherapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val newTitle = randomCyrillicWord()
        val addedExercise = backgrounds.exercises.createExercise()
        val removedExercise = program.exercises[1].exerciseRef.resolveOrThrow()

        val updateProgramRequest = randomCreateProgramRequest(
            newTitle,
            program.exercises.map { it.exerciseRef.resolveOrThrow() } - removedExercise + addedExercise)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.programs.updateProgram(program.id, updateProgramRequest, newTherapeuticTask.name)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()
        response.header("HX-Redirect") shouldBe ProgramsListPage.path

        val updatedProgram = backgrounds.programs.findAll().single()
        updatedProgram shouldMatch (updateProgramRequest to newTherapeuticTask.name)
    }

    @Test
    fun `Request for edit page of not existing program should return standard 404 error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.getProgramEditPage(-1, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `When user tries to edit program with not existing therapeutic task name failed validation response should be returned`() {
        // Given
        val program = backgrounds.programs.createRandomProgram()
        val notExistingTherapeuticTask = randomCyrillicWord()

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.updateProgramForError(
            program.id,
            CreateProgramRequest(program.title, program.exercises.map { it.exerciseRef.id!! }),
            notExistingTherapeuticTask
        )

        // Then
        document shouldHave CreateProgramForm.notExistingTherapeuticTaskMessage
    }

    @Test
    fun `Edit of not existing program should return standard 404 error page`() {
        // Given
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.updateProgramForError(
            -1,
            randomCreateProgramRequest(exercises = listOf(ExercisesObjectMother.randomExercise().first)),
            therapeuticTask.name,
            expectedStatus = HttpStatus.NOT_FOUND
        )

        // Then
        document shouldBePage NotFoundErrorPage
    }

}