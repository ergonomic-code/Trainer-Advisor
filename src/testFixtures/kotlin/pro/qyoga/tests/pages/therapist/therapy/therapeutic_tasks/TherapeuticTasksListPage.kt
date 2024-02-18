package pro.qyoga.tests.pages.therapist.therapy.therapeutic_tasks

import io.kotest.inspectors.forAll
import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.platform.html.*

object TherapeuticTasksListPage : QYogaPage {

    override val path = "/therapist/therapeutic-tasks"

    override val title = "Терапевтические задачи"

    object SearchTasksForm : QYogaForm("searchTasksForm", FormAction.hxGet("$path/search")) {

        val searchKey = Input.text("searchKey", required = false)

        override val components = listOf(
            searchKey,
        )

    }

    object AddTaskForm : QYogaForm("addTaskForm", FormAction.hxPost(path)) {

        const val DUPLICATED_NAME_MESSAGE = "#duplicatedNewTaskName"

        val taskName = Input.text("taskName", true)

        override val components = listOf(
            taskName,
            Button("addTaskButton", "")
        )

    }


    object EditTaskForm : QYogaForm("", FormAction.hxPut("$path/{taskId}"), "editTaskForm") {

        val taskNameInput = Input.text("taskName", true)

        const val SUCCESS_MESSAGE = "#editSuccessMessage"

        const val DUPLICATED_EDITED_NAME_MESSAGE = "input[name=taskName].is-invalid"

        const val TASK_HAS_REFERENCES_ERROR_MESSAGE = "#taskHasReferencesMessage"

        override val components = listOf(
            taskNameInput,
            Button("editTaskButton", ""),
            Button("deleteTaskButton", "", FormAction.hxDelete(action.url))
        )

    }

    const val taskRow = "div.taskRow"

    override fun match(element: Element) {
        element shouldHaveComponent SearchTasksForm
        element shouldHaveComponent AddTaskForm
    }

    fun rowFor(task: TherapeuticTask): PageMatcher = object : PageMatcher {
        override fun match(element: Element) {
            val rows = element.select(taskRow)
            rows.forAny {
                it shouldBeRowFor task
            }
        }
    }

    fun rowsFor(expectedSearchResult: List<TherapeuticTask>): PageMatcher = object : PageMatcher {
        override fun match(element: Element) {
            val elements = element.select(taskRow)

            elements.zip(expectedSearchResult).forAll { (el, task) ->
                el shouldBeRowFor task
            }
        }

    }

    infix fun Element.shouldBeRowFor(task: TherapeuticTask) {
        this shouldHaveComponent EditTaskForm
        this.select(EditTaskForm.taskNameInput.selector()).`val`() shouldBe task.name
    }

}