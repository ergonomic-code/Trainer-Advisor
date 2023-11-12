package pro.qyoga.cases.app.therapist.programs.exercises

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import pro.qyoga.assertions.shouldBe
import pro.qyoga.assertions.shouldMatch
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.therapist.programs.exercises.CreateExercisePage
import pro.qyoga.fixture.ImagesObjectMother
import pro.qyoga.fixture.programs.exercises.ExercisesObjectMother
import pro.qyoga.fixture.programs.exercises.ExercisesObjectMother.createExerciseRequest
import pro.qyoga.fixture.programs.exercises.ExercisesObjectMother.exerciseStepDto
import pro.qyoga.infra.web.QYogaAppBaseTest
import java.time.Duration


class CreateExercisePageTest : QYogaAppBaseTest() {

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
            step1.idx to ImagesObjectMother.image(),
            step2.idx to ImagesObjectMother.image()
        )

        // When
        therapist.exercises.createExercise(createExerciseRequest, images)

        // Then
        val exercise = backgrounds.exercises.findExercise(ExercisesObjectMother.exerciseSearchDto(title))
        exercise shouldNotBe null
        exercise!! shouldMatch createExerciseRequest
    }

}