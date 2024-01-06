package pro.qyoga.tests.cases.app.therapist.therapy.exercises

import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.therapy.exercises.CreateExercisePageController
import pro.qyoga.core.therapy.exercises.api.ExerciseSearchDto
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.FilesObjectMother
import pro.qyoga.tests.fixture.therapists.theTherapistUserDetails
import pro.qyoga.tests.fixture.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest

class CreateExercisePageControllerTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Exercise without images should be correctly persisted`() {
        // Given
        val exercise = ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseStepDtos(3) }

        // When
        getBean<CreateExercisePageController>().createExercise(exercise, emptyMap(), theTherapistUserDetails)

        // Then
        val persistedExercise = backgrounds.exercises.findExercise(ExerciseSearchDto.ALL)
        persistedExercise!! shouldMatch exercise
    }

    @Test
    fun `Exercise for images for even steps should be correctly persisted`() {
        // Given
        val exercise = ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseStepDtos(4) }
        val img1 = FilesObjectMother.imageAsMultipartFile()
        val img2 = FilesObjectMother.imageAsMultipartFile()
        val images = ExercisesObjectMother.exerciseImages(
            0 to img1,
            2 to img2
        )

        // When
        getBean<CreateExercisePageController>().createExercise(exercise, images, theTherapistUserDetails)

        // Then
        val persistedExercise = backgrounds.exercises.findExercise(ExerciseSearchDto.ALL)
        persistedExercise!! shouldMatch exercise
        // TODO: проверка файлов после появления АПИ их получения
    }

    @Test
    fun `Exercise for images for odd steps should be correctly persisted`() {
        // Given
        val exercise = ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseStepDtos(4) }
        val img1 = FilesObjectMother.imageAsMultipartFile()
        val img2 = FilesObjectMother.imageAsMultipartFile()
        val images = ExercisesObjectMother.exerciseImages(
            1 to img1,
            3 to img2
        )

        // When
        getBean<CreateExercisePageController>().createExercise(exercise, images, theTherapistUserDetails)

        // Then
        val persistedExercise = backgrounds.exercises.findExercise(ExerciseSearchDto.ALL)
        persistedExercise!! shouldMatch exercise
        // TODO: проверка файлов после появления АПИ их получения
    }

}