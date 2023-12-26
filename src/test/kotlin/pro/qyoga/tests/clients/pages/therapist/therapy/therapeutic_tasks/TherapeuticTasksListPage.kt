package pro.qyoga.tests.clients.pages.therapist.therapy.therapeutic_tasks

import io.kotest.inspectors.forAll
import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.*

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

        override val components = listOf(
            Input.text("taskName", true),
            Button("addTaskButton", "")
        )

    }

    object EditTaskForm : QYogaForm("", FormAction.hxPut(path)) {

        val taskNameInput = Input.text("taskName", true)

        override val components = listOf(
            taskNameInput,
            Button("editTaskButton", ""),
            Button("deleteTaskButton", "")
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
        this.select(EditTaskForm.taskNameInput.selector()).`val`() shouldBe task.name
    }

}