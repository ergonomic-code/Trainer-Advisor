package pro.qyoga.tests.clients.pages.therapist.therapy.exercises

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.QYogaPage


object CreateExercisePage : QYogaPage {

    override val path = "/therapist/exercises/create"

    override val title = "Новое упражнение"

    override fun match(element: Element) {
        element.select("title")[0].text() shouldBe title

        element shouldHaveComponent CreateExerciseForm
    }

}