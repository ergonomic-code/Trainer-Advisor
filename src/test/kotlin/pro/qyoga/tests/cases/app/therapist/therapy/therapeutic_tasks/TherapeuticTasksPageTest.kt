package pro.qyoga.tests.cases.app.therapist.therapy.therapeutic_tasks

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.therapy.therapeutic_tasks.TherapeuticTasksListPage
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class TherapeuticTasksPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Therapeutic tasks page should be rendered correctly when no tasks exist`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.getTasksListPage()

        // Then
        document shouldBe TherapeuticTasksListPage
    }

    @Test
    fun `When therapeutic task exist it should be displayed on tasks list page`() {
        // Given
        val taskName = "ЙТ Гастрита"
        val task = backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID, taskName)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.getTasksListPage()

        // Then
        document shouldHave TherapeuticTasksListPage.rowFor(task)
    }

}