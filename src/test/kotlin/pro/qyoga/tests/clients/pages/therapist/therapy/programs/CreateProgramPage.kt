package pro.qyoga.tests.clients.pages.therapist.therapy.programs

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.FormAction
import pro.qyoga.tests.infra.html.Input
import pro.qyoga.tests.infra.html.QYogaForm
import pro.qyoga.tests.infra.html.QYogaPage


object CreateProgramPage : QYogaPage {

    override val path = "/therapist/programs/create"

    const val SEARCH_EXERCISE_PATH = "/therapist/programs/create/search-exercises"

    const val SEARCH_KEY = "searchKey"

    override val title = "Новая программа"

    object CreateProgramForm : QYogaForm("programForm", FormAction.hxPost(path)) {

        val titleInput = Input.text("title", true)

        val therapeuticTaskInput = Input.text("therapeuticTaskName", true)

        const val EXERCISE_IDS_INPUT_NAME = "exerciseIds"

        const val NOT_EXISTING_THERAPEUTIC_TASK_MESSAGE =
            "div.invalid-feedback:contains(Терапевтической задачи с таким названием не существует)"

        override val components = listOf(
            titleInput,
            therapeuticTaskInput
        )

    }

    private const val PROGRAM_FORM_SCRIPT = "programFormScript"

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element shouldHaveComponent CreateProgramForm

        element shouldHave "script#$PROGRAM_FORM_SCRIPT"
        val programFormScript = element.getElementById(PROGRAM_FORM_SCRIPT)!!
        programFormScript.html() shouldContain "$SEARCH_EXERCISE_PATH?$SEARCH_KEY"
    }

}