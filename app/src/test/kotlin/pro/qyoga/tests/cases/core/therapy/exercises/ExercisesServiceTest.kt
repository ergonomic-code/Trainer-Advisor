package pro.qyoga.tests.cases.core.therapy.exercises

import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.springframework.jdbc.CannotGetJdbcConnectionException
import pro.azhidkov.platform.file_storage.api.FilesStorage
import pro.qyoga.core.therapy.exercises.impl.ExercisesRepo
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.AllSteps
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class ExercisesServiceTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Exercise deletion should fail in case of exercise deletion failure`() {
        // Given
        val exercisesRepo = mock<ExercisesRepo> {
            on { findById(any()) } doThrow CannotGetJdbcConnectionException("Это ожидаемое в тесте исключение")
        }
        val exerciseId = backgrounds.exercises.createExercise(stepsCount = 1, AllSteps).id
        val exercisesService = backgrounds.spring.createExercisesService(exercisesRepo = exercisesRepo)

        // When
        val result = runCatching {
            exercisesService.deleteById(exerciseId)
        }

        // Then
        result.shouldBeFailure<CannotGetJdbcConnectionException>()
    }

    @Test
    fun `Exercise deletion should complete successfully even in case of steps images deletion failure`() {
        // Given
        val filesStorage = mock<FilesStorage> {
            on { deleteAllById(any()) } doAnswer { throw Exception("Это ожидаемое в тесте исключение") }
        }
        val exerciseId = backgrounds.exercises.createExercise(stepsCount = 1, AllSteps).id
        val exercisesService = backgrounds.spring.createExercisesService(exerciseStepsImagesStorage = filesStorage)

        // When
        val result = runCatching {
            exercisesService.deleteById(exerciseId)
        }

        // Then
        result.shouldBeSuccess()
        exercisesService.findById(exerciseId) shouldBe null
    }

}

