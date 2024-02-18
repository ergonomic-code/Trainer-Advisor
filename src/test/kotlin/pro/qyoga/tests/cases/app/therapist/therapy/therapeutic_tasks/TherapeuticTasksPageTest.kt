package pro.qyoga.tests.cases.app.therapist.therapy.therapeutic_tasks

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.therapy.therapeutic_tasks.TherapeuticTasksListPage
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapy.therapeutic_tasks.SearchTherapeuticTasksFixture
import pro.qyoga.tests.fixture.object_mothers.therapy.therapeutic_tasks.TherapeuticTasksObjectMother.therapeuticTask
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class TherapeuticTasksPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Therapeutic tasks page should be rendered correctly when no tasks exist`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.getTasksListPage()

        // Then
        document shouldBePage TherapeuticTasksListPage
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
        document shouldBeElement TherapeuticTasksListPage.rowsFor(expectedSearchResult)
    }

    @Test
    fun `Creation of therapeutic task should be persistent`() {
        // Given
        val task = therapeuticTask()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.create(task)

        // Then
        document shouldHaveComponent TherapeuticTasksListPage.AddTaskForm
        document shouldHave TherapeuticTasksListPage.rowFor(task)
    }

    @Test
    fun `When user creates new therapeutic task with exiting name form with error message should be returned`() {
        // Given
        val task = therapeuticTask()
        val therapist = TherapistClient.loginAsTheTherapist()
        backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID, task.name)

        // When
        val document = therapist.therapeuticTasks.create(task)

        // Then
        document shouldHaveComponent TherapeuticTasksListPage.AddTaskForm
        document shouldHave TherapeuticTasksListPage.AddTaskForm.DUPLICATED_NAME_MESSAGE
        document shouldNotHave TherapeuticTasksListPage.taskRow
    }

    @Test
    fun `Editing of therapeutic task should be persistent`() {
        // Given
        var task = therapeuticTask()
        task = backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID, task.name)

        val newName = "newName"
        val updatedTask = task.withName(newName)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.edit(updatedTask)

        // Then
        document shouldHave TherapeuticTasksListPage.EditTaskForm.SUCCESS_MESSAGE
        backgrounds.therapeuticTasks.findById(task.id) shouldMatch updatedTask
    }

    @Test
    fun `If user changes therapeutic task name to name of existing task error message should be show and change should not be persistent`() {
        // Given
        val theName = randomCyrillicWord()
        val (_, task2) = backgrounds.therapeuticTasks.createTherapeuticTasks(
            THE_THERAPIST_ID,
            listOf(theName, randomCyrillicWord())
        )
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.edit(task2.withName(theName))

        // Then
        document shouldHaveComponent TherapeuticTasksListPage.EditTaskForm
        document shouldHave TherapeuticTasksListPage.EditTaskForm.DUPLICATED_EDITED_NAME_MESSAGE
        backgrounds.therapeuticTasks.findById(task2.id) shouldMatch task2
    }

    @Test
    fun `Deletion of therapeutic task should be persistent`() {
        // Given
        val task = backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.therapeuticTasks.delete(task.id)

        // Then
        val deletedTask = backgrounds.therapeuticTasks.findById(task.id)
        deletedTask shouldBe null
    }

    @Test
    fun `When user tries to delete referenced therapeutic task error message should be returned and task should remain persistent`() {
        // Given
        val (_, entry) = backgrounds.clients.createClientWithJournalEntry()
        val therapist = TherapistClient.loginAsTheTherapist()
        val therapeuticTask = entry.therapeuticTask.resolveOrThrow()

        // When
        val document = therapist.therapeuticTasks.delete(therapeuticTask.id)

        // Then
        document shouldHave TherapeuticTasksListPage.EditTaskForm.TASK_HAS_REFERENCES_ERROR_MESSAGE
        backgrounds.therapeuticTasks.findById(therapeuticTask.id) shouldMatch therapeuticTask
    }

}