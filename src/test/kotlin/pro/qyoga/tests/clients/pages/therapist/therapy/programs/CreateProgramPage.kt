package pro.qyoga.tests.clients.pages.therapist.therapy.programs

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.FormAction
import pro.qyoga.tests.infra.html.Input
import pro.qyoga.tests.infra.html.QYogaForm
import pro.qyoga.tests.infra.html.QYogaPage


object CreateProgramPage : QYogaPage {

    override val path = "/therapist/programs/create"

    override val title = "Новая программа"

    object CreateProgramForm : QYogaForm("editProgram", FormAction.hxPost(path)) {

        val titleInput = Input.text("title", true)

        val therapeuticTaskInput = Input.text("therapeuticTaskName", true)

        val exerciseIdInputName = "exerciseIds"

        override val components = listOf(
            titleInput,
            therapeuticTaskInput
        )

    }

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element shouldHaveComponent CreateProgramForm
    }

}