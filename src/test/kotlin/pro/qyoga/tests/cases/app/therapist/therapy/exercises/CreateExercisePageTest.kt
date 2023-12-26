package pro.qyoga.tests.cases.app.therapist.therapy.exercises

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.therapy.exercises.CreateExercisePage
import pro.qyoga.tests.fixture.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.fixture.therapy.exercises.ExercisesObjectMother.createExerciseRequest
import pro.qyoga.tests.fixture.therapy.exercises.ExercisesObjectMother.exerciseStepDto
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
        val step1 = exerciseStepDto(0, "Take a deep breath")
        val step2 = exerciseStepDto(1, "And exhale")

        val createExerciseRequest = createExerciseRequest(title, description, duration) {
            listOf(step1, step2)
        }

        val images = mapOf(
            step1.idx to pro.qyoga.tests.fixture.ImagesObjectMother.image(),
            step2.idx to pro.qyoga.tests.fixture.ImagesObjectMother.image()
        )

        // When
        therapist.exercises.createExercise(createExerciseRequest, images)

        // Then
        val exercise = backgrounds.exercises.findExercise(ExercisesObjectMother.exerciseSearchDto(title))
        exercise shouldNotBe null
        exercise!! shouldMatch createExerciseRequest
    }

}