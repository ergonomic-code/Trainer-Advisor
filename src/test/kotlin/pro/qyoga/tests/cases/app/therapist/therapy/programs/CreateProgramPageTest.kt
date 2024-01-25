package pro.qyoga.tests.cases.app.therapist.therapy.programs

import io.restassured.module.kotlin.extensions.Then
import org.junit.jupiter.api.Test
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class CreateProgramPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Create program page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.getCreateProgramPage()
    }

    @Test
    fun `Program creation should be persistent`() {
        // Given
        val exercises = backgrounds.exercises.createExercises(ExercisesObjectMother.createExerciseRequests(3))
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val title = randomCyrillicWord()
        val createProgramRequest = CreateProgramRequest(title, exercises.map { it.id })

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.programs.createProgram(createProgramRequest, therapeuticTask.name)

        response.Then {
            header("HX-Redirect", ProgramsListPage.path)
        }

        val program = backgrounds.programs.findAll().single()
        program shouldMatch (createProgramRequest to therapeuticTask.name)
    }

}