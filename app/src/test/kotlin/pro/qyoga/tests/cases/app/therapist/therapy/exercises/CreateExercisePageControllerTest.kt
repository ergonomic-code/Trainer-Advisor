package pro.qyoga.tests.cases.app.therapist.therapy.exercises

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.therapy.exercises.CreateExercisePageController
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.object_mothers.FilesObjectMother.randomImageAsMultipartFile
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest

class CreateExercisePageControllerTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Exercise without images should be correctly persisted`() {
        // Given
        val exercise = ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseSteps(3) }

        // When
        getBean<CreateExercisePageController>().createExercise(exercise, emptyMap(), theTherapistUserDetails)

        // Then
        val persistedExercise = backgrounds.exercises.findExerciseSummary(ExerciseSearchDto.ALL)
        persistedExercise!! shouldMatch exercise.summary
    }

    @Test
    fun `Exercise with images for even steps should be correctly persisted`() {
        // Given
        val exercise = ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseSteps(4) }
        val img1 = randomImageAsMultipartFile()
        val img2 = randomImageAsMultipartFile()
        val zeroStepIdx = 0
        val secondStepIdx = 2
        val images = ExercisesObjectMother.exerciseImages(
            zeroStepIdx to img1,
            2 to img2
        )

        // When
        getBean<CreateExercisePageController>().createExercise(exercise, images, theTherapistUserDetails)

        // Then
        val persistedExercise = backgrounds.exercises.findExerciseSummary(ExerciseSearchDto.ALL)
        persistedExercise!! shouldMatch exercise.summary
        val loadedImg1 = backgrounds.exercises.getExerciseStepImage(persistedExercise.id, zeroStepIdx)
        loadedImg1 shouldBe img1.bytes
        val loadedImg2 = backgrounds.exercises.getExerciseStepImage(persistedExercise.id, secondStepIdx)
        loadedImg2 shouldBe img2.bytes
    }

    @Test
    fun `Exercise with images for odd steps should be correctly persisted`() {
        // Given
        val firstStepIdx = 1
        val thirdStepIdx = 3
        val exercise = ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseSteps(4) }
        val img1 = randomImageAsMultipartFile()
        val img2 = randomImageAsMultipartFile()
        val images = ExercisesObjectMother.exerciseImages(
            firstStepIdx to img1,
            thirdStepIdx to img2
        )

        // When
        getBean<CreateExercisePageController>().createExercise(exercise, images, theTherapistUserDetails)

        // Then
        val persistedExercise = backgrounds.exercises.findExerciseSummary(ExerciseSearchDto.ALL)
        persistedExercise!! shouldMatch exercise.summary
        val loadedImg1 = backgrounds.exercises.getExerciseStepImage(persistedExercise.id, firstStepIdx)
        loadedImg1 shouldBe img1.bytes
        val loadedImg2 = backgrounds.exercises.getExerciseStepImage(persistedExercise.id, thirdStepIdx)
        loadedImg2 shouldBe img2.bytes
    }

    @Test
    fun `When therapist creates exercise with two images with same name they both should be stored`() {
        // Given
        val imgBaseName = "img"
        val format = "png"
        val exercise = ExercisesObjectMother.createExerciseRequest { ExercisesObjectMother.exerciseSteps(2) }
        val img1 = randomImageAsMultipartFile(imgBaseName, format)
        val img2 = randomImageAsMultipartFile(imgBaseName, format)
        val zeroStepIdx = 0
        val firstStepIdx = 1
        val images = ExercisesObjectMother.exerciseImages(
            zeroStepIdx to img1,
            firstStepIdx to img2
        )

        // When
        getBean<CreateExercisePageController>().createExercise(exercise, images, theTherapistUserDetails)

        // Then
        val persistedExercise = backgrounds.exercises.findExerciseSummary(ExerciseSearchDto.ALL)
        persistedExercise!! shouldMatch exercise.summary
        val loadedImg1 = backgrounds.exercises.getExerciseStepImage(persistedExercise.id, zeroStepIdx)
        loadedImg1 shouldBe img1.bytes
        val loadedImg2 = backgrounds.exercises.getExerciseStepImage(persistedExercise.id, firstStepIdx)
        loadedImg2 shouldBe img2.bytes
    }

}