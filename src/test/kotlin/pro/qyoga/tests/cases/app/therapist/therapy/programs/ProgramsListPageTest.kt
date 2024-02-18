package pro.qyoga.tests.cases.app.therapist.therapy.programs

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.therapy.programs.ProgramsListPage
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

    @Test
    fun `Search for programs without parameters should return up 10 least programs in alphabetical order`() {
        // Given
        val pageSize = 10
        val programs = backgrounds.programs.createPrograms(pageSize + 1)
        val firstPage = programs.sortedBy { it.title.lowercase() }
            .take(pageSize)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.searchPrograms(ProgramsSearchFilter())

        // Then
        document shouldHave ProgramsListPage.rowsFor(firstPage)
    }

    @Test
    fun `Result of search programs with therapeutic task name should contain all matching programs and only matching programs`() {
        // Given
        val keyword = "Коррекция"
        val task1 = backgrounds.therapeuticTasks.createTherapeuticTask(taskName = "Де$keyword гиперлордоза")
        val task2 = backgrounds.therapeuticTasks.createTherapeuticTask(taskName = "$keyword гипокифоза")
        val matchingPrograms =
            (backgrounds.programs.createPrograms(1, task1) + backgrounds.programs.createPrograms(1, task2))
                .sortedBy { it.title.lowercase() }
        backgrounds.programs.createPrograms(3)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.searchPrograms(ProgramsSearchFilter(therapeuticTaskKeyword = keyword))

        // Then
        document shouldHave ProgramsListPage.rowsFor(matchingPrograms)
    }

    @Test
    fun `Result of search programs with program title should contain all matching programs and only matching programs`() {
        // Given
        val keyword = "Коррекция"
        val matchingPrograms =
            (listOf(backgrounds.programs.createRandomProgram(title = "$keyword гиполардоза")) + backgrounds.programs.createRandomProgram(
                title = "Де$keyword гиперкифоза"
            ))
                .sortedBy { it.title.lowercase() }
        backgrounds.programs.createPrograms(3)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.programs.searchPrograms(ProgramsSearchFilter(titleKeyword = keyword))

        // Then
        document shouldHave ProgramsListPage.rowsFor(matchingPrograms)
    }

}