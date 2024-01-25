package pro.qyoga.tests.cases.app.therapist.therapy.programs

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


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

        // When
        val docxFile = theTherapist.programs.downloadDocx(program.id)
    }

}