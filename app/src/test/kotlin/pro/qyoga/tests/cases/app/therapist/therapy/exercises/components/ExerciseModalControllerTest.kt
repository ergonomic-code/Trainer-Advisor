package pro.qyoga.tests.cases.app.therapist.therapy.exercises.components

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother.createExerciseRequests
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.therapy.exercises.ExerciseModal


@DisplayName("Диалоговое окно просмотра упражнения")
class ExerciseModalControllerTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `должно отображаться корректно`() {
        // Дано
        val exercise = backgrounds.exercises.createExercises(createExerciseRequests(1)).single()
        val therapist = TherapistClient.loginAsTheTherapist()

        // Когда
        val document = therapist.exercises.getExerciseModal(exercise.id)

        // Тогда
        document shouldBe ExerciseModal.forExercise(exercise)
    }

    @Test
    fun `должно возвращать страницу ошибки 404 при запросе несуществующего упражнения`() {
        // Дано
        val therapist = TherapistClient.loginAsTheTherapist()

        // Когда
        val document = therapist.exercises.getExerciseModal(404, expectedStatus = HttpStatus.NOT_FOUND)

        // Тогда
        document shouldBePage NotFoundErrorPage
    }

}