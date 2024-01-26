package pro.qyoga.tests.cases.app.therapist.therapy.programs

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.io.ByteArrayInputStream


class ProgramsListPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Programs page without programs should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.getProgramsListPage()

        // Then
        document shouldBePage ProgramsListPage
    }

    @Test
    fun `Programs list page should contain 10 rows for first 10 programs in alphabetical order in db`() {
        // Given
        val pageSize = 10
        val programs = backgrounds.programs.createPrograms(pageSize + 1)
        val firstPage = programs.sortedBy { it.title.lowercase() }
            .take(pageSize)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.getProgramsListPage()

        // Then
        document shouldHave ProgramsListPage.rowsFor(firstPage)
    }

    @Test
    fun `Program docx downloading link should return valid docx file`() {
        // Given
        val program = backgrounds.programs.createRandomProgram(exercisesCount = 3, stepsInEachExercise = 3)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val docxFile = therapist.programs.downloadDocx(program.id)

        // Then
        val openResult = runCatching { XWPFDocument(ByteArrayInputStream(docxFile)) }
        openResult.shouldBeSuccess()
    }

    @Test
    fun `Program docx downloading link should return 404 error on request for not existing program`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val docxFile = therapist.programs.downloadDocx(-1, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        docxFile shouldBe byteArrayOf()
    }

    @Test
    fun `Program deletion should be persistent`() {
        // Given
        val program = backgrounds.programs.createRandomProgram()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.programs.deleteProgram(program.id)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()
        backgrounds.programs.findAll().toList() shouldHaveSize 0
    }

}