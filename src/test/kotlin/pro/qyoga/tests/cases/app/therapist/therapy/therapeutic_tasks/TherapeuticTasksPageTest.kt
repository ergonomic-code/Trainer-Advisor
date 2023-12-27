package pro.qyoga.tests.cases.app.therapist.therapy.therapeutic_tasks

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.therapy.therapeutic_tasks.TherapeuticTasksListPage
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.therapy.therapeutic_tasks.SearchTherapeuticTasksFixture
import pro.qyoga.tests.fixture.therapy.therapeutic_tasks.TherapeuticTasksObjectMother.therapeuticTask
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

    @Test
    fun `Therapist tasks search endpoint should return tasks containing search keyword in any place and register`() {
        // Given
        val therapeuticTasks = backgrounds.therapeuticTasks.createTherapeuticTasks(
            THE_THERAPIST_ID,
            SearchTherapeuticTasksFixture.matchingTaskNames + SearchTherapeuticTasksFixture.notMatchingTaskNames
        )
        val expectedSearchResult = SearchTherapeuticTasksFixture.getExpectedSearchResult(
            therapeuticTasks,
            SearchTherapeuticTasksFixture.searchKey, 5
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.search(SearchTherapeuticTasksFixture.searchKey)

        // Then
        document shouldBe TherapeuticTasksListPage.rowsFor(expectedSearchResult)
    }

    @Test
    fun `Creation of therapeutic task should be peristent`() {
        // Given
        val task = therapeuticTask()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.create(task)

        // Then
        document shouldHaveComponent TherapeuticTasksListPage.AddTaskForm
        document shouldHave TherapeuticTasksListPage.rowFor(task)
    }

}