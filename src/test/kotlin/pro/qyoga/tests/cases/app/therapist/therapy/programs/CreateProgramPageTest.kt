package pro.qyoga.tests.cases.app.therapist.therapy.programs

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveAtMostSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.therapy.programs.CreateProgramForm
import pro.qyoga.tests.pages.therapist.therapy.programs.CreateProgramPage
import pro.qyoga.tests.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother.createExerciseRequest
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother.createExerciseRequests
import pro.qyoga.tests.fixture.object_mothers.therapy.programs.ProgramsObjectMother.randomCreateProgramRequest
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class CreateProgramPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Create program page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.getCreateProgramPage()

        // Then
        document shouldBePage CreateProgramPage
    }

    @Test
    fun `Program creation should be persistent`() {
        // Given
        val exercises = backgrounds.exercises.createExercises(createExerciseRequests(3))
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val title = randomCyrillicWord()
        val createProgramRequest = randomCreateProgramRequest(title, exercises)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.programs.createProgram(createProgramRequest, therapeuticTask.name)

        // Then
        response.header("HX-Redirect") shouldBe ProgramsListPage.path

        val program = backgrounds.programs.findAll().single()
        program shouldMatch (createProgramRequest to therapeuticTask.name)
    }

    @Test
    fun `When user tries to create program with not existing therapeutic task name failed validation response should be returned`() {
        // Given
        val exercises = backgrounds.exercises.createExercises(createExerciseRequests(3))
        val createProgramRequest = randomCreateProgramRequest(exercises = exercises)
        val notExistingTherapeuticTask = randomCyrillicWord()

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.createProgramForError(createProgramRequest, notExistingTherapeuticTask)

        // Then
        document shouldHaveComponent CreateProgramForm
        CreateProgramForm.titleInput.value(document) shouldBe createProgramRequest.title
        CreateProgramForm.therapeuticTaskInput.value(document) shouldBe notExistingTherapeuticTask
        document shouldHave CreateProgramForm.notExistingTherapeuticTaskMessage
    }

    @Test
    fun `Search for exercises by key word should return up 5 exercises, that contains keyword in title`() {
        // Given
        val keyword = "ПИР"
        val pageSize = 5

        val matchingExercise1 = createExerciseRequest(title = "$keyword флексоров шеи")
        val matchingExercise2 = createExerciseRequest(title = "$keyword бицепса бедра")
        val arbitraryExercises = createExerciseRequests(pageSize)
        backgrounds.exercises.createExercises(arbitraryExercises + matchingExercise1 + matchingExercise2)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val exercises = therapist.programs.searchExercises(keyword)

        // Then
        exercises shouldHaveAtLeastSize 2
        exercises shouldHaveAtMostSize pageSize
        exercises.toList().forAll { it.title shouldContain keyword }
    }

}