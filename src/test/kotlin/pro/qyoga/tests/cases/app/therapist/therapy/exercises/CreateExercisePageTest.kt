package pro.qyoga.tests.cases.app.therapist.therapy.exercises

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.therapy.exercises.CreateExercisePage
import pro.qyoga.tests.fixture.object_mothers.FilesObjectMother.randomImage
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother.createExerciseRequest
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother.exerciseSteps
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.Duration


class CreateExercisePageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Create exercise page should be correctly rendered`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.exercises.getCreateExercisesPage()

        // Then
        document shouldBe CreateExercisePage
    }

    @Test
    fun `Created exercise should appear in exercises list`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        val title = "Just added exercise"
        val description = "Description of just added exercise"
        val duration = Duration.ofMinutes(2)
        val step1 = exerciseSteps("Take a deep breath")
        val step2 = exerciseSteps("And exhale")
        val firstStepIdx = 0
        val secondStepIdx = 1

        val createExerciseRequest = createExerciseRequest(title, description, duration) {
            listOf(step1, step2)
        }

        val images = mapOf(
            firstStepIdx.toLong() to randomImage(),
            secondStepIdx.toLong() to randomImage()
        )

        // When
        therapist.exercises.createExercise(createExerciseRequest, images)

        // Then
        val exerciseSummary = backgrounds.exercises.findExerciseSummary(ExercisesObjectMother.exerciseSearchDto(title))
        exerciseSummary shouldNotBe null
        exerciseSummary!! shouldMatch createExerciseRequest.summary

        // And then
        val exercise = backgrounds.exercises.findExercise(exerciseSummary.id)
        exercise.steps shouldHaveSize createExerciseRequest.steps.size
        exercise.steps[firstStepIdx].description shouldBe step1.description
        exercise.steps[secondStepIdx].description shouldBe step2.description

        val img1 = backgrounds.exercises.getExerciseStepImage(exerciseSummary.id, firstStepIdx)
        img1 shouldBe images[firstStepIdx.toLong()]!!.content

        val img2 = backgrounds.exercises.getExerciseStepImage(exerciseSummary.id, secondStepIdx)
        img2 shouldBe images[secondStepIdx.toLong()]!!.content
    }

}