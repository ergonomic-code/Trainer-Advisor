package pro.qyoga.tests.clients.pages.therapist.therapy.therapeutic_tasks

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.*

object TherapeuticTasksListPage : QYogaPage {

    val basePath = "/therapist/therapeutic-tasks"
    override val path = "$basePath/list"

    override val title = "Терапевтические задачи"

    object SearchTasksForm : QYogaForm("searchTasksForm", FormAction.hxGet(path)) {

        override val components = listOf(
            Input.text("searchKey", true),
            Button("searchTasksButton", "")
        )

    }

    object AddTaskForm : QYogaForm("addTaskForm", FormAction.hxPost(basePath)) {

        override val components = listOf(
            Input.text("taskName", true),
            Button("addTaskButton", "")
        )

    }

    object EditTaskForm : QYogaForm("", FormAction.hxPut(basePath)) {

        val taskNameInput = Input.text("taskName", true)

        override val components = listOf(
            taskNameInput,
            Button("editTaskButton", ""),
            Button("deleteTaskButton", "")
        )

    }

    override fun match(element: Element) {
        element shouldHaveComponent SearchTasksForm
        element shouldHaveComponent AddTaskForm
    }

    fun rowFor(task: TherapeuticTask): PageMatcher = object : PageMatcher {
        override fun match(element: Element) {
            val rows = element.select(".taskRow")
            rows.forAny {
                it.select(EditTaskForm.taskNameInput.selector()).`val`() shouldBe task.name
            }
        }
    }

}