package pro.qyoga.tests.cases.app.therapist.therapy.programs

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.therapy.programs.list.ProgramsListPageController
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.AllSteps
import pro.qyoga.tests.infra.web.QYogaAppBaseTest


@DisplayName("Операция генерации docx-а с программой")
class GenerateProgramDocxControllerTest : QYogaAppBaseTest() {

    @DisplayName("должна генерировать валидный документ со всеми изображениями")
    @Test
    fun generateProgram() {
        // Given
        val exercisesCount = 2
        val stepsInEachExercise = 2
        val totalStepsCount = exercisesCount * stepsInEachExercise

        val program = backgrounds.programs.createRandomProgram(
            exercisesCount = exercisesCount,
            stepsInEachExercise = stepsInEachExercise,
            imagesGenerationMode = AllSteps
        )

        val images = backgrounds.programs.fetchExerciseImages(program)
        val programId = program.id

        val programsListPageController = getBean<ProgramsListPageController>()

        // When
        val docxResponse = programsListPageController.getProgramDocx(programId)

        // Then
        docxResponse.statusCode shouldBe HttpStatus.OK

        // And then
        val openResult = runCatching { XWPFDocument(docxResponse.body!!.inputStream) }
        openResult.shouldBeSuccess()
        val doc = openResult.getOrThrow()

        // And then
        doc.allPictures shouldHaveSize totalStepsCount
        doc.allPictures.zip(images).forAll { (docxImg, img) ->
            docxImg.data shouldBe img
        }
    }

}