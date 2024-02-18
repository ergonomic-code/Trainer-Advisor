package pro.qyoga.tests.cases.app.therapist.therapy.exercises

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.therapy.exercises.EditExercisePage
import pro.qyoga.tests.fixture.object_mothers.FilesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother.createExerciseRequests
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest

class EditExercisePageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Edit exercise page should be correctly rendered`() {
        // Given
        val exercise = backgrounds.exercises.createExercises(createExerciseRequests(1)).single()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.exercises.getEditExercisePage(exercise.id)

        // Then
        document shouldBe EditExercisePage.pageFor(exercise)
    }

    @Test
    fun `Exercise editing should be persistent`() {
        // Given
        val exercise = backgrounds.exercises.createExercises(createExerciseRequests(1)).single()
        val updatedExercise = ExercisesObjectMother.exerciseSummary()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.exercises.editExercise(exercise.id, updatedExercise)

        // Then
        backgrounds.exercises.findExerciseSummary(ExerciseSearchDto(updatedExercise.title)) shouldMatch updatedExercise
    }

    @Test
    fun `Request of edit exercise page by not existing id should return 404 error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.exercises.getEditExercisePage(-1, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `When exercise step image is requested correct file should be returned`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val exercise = ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseSteps(1) }
        val img = FilesObjectMother.randomImage()
        val stepIdx = 0
        val exerciseId =
            backgrounds.exercises.createExercises(listOf(exercise), listOf(mapOf(stepIdx to img))).single().id

        // When
        val loadedImg = therapist.exercises.getStepImage(exerciseId, stepIdx)

        // Then
        loadedImg shouldBe img.content
    }

    @Test
    fun `Request of image of not existing exercise should return 404 error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.exercises.getStepImage(-1, -1, HttpStatus.NOT_FOUND)

        // Then
        response shouldBePage NotFoundErrorPage
    }

    @Test
    fun `Request of image of not existing exercise step should return 404 error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val stepsCount = 1
        val exercise =
            ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseSteps(stepsCount) }
        val exerciseId = backgrounds.exercises.createExercises(listOf(exercise)).single().id

        // When
        val response = therapist.exercises.getStepImage(exerciseId, stepIdx = stepsCount, HttpStatus.NOT_FOUND)

        // Then
        response shouldBePage NotFoundErrorPage
    }

}