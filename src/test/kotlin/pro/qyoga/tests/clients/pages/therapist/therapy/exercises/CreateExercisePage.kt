package pro.qyoga.tests.clients.pages.therapist.therapy.exercises

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.*
import pro.qyoga.tests.infra.html.Input.Companion.button
import pro.qyoga.tests.infra.html.Input.Companion.file
import pro.qyoga.tests.infra.html.Input.Companion.number
import pro.qyoga.tests.infra.html.Input.Companion.submit
import pro.qyoga.tests.infra.html.Input.Companion.text


object CreateExercisePage : QYogaPage {

    override val path = "/therapist/exercises/create"

    override val title = "Новое упражнение"

    object CreateExerciseForm : QYogaForm("createExercise", FormAction.hxPost(path)) {
        val title = text("title", true)

        val duration = number("duration", true)
        val exerciseType = Select("exerciseType", true)
        val description = TextArea("description", true)

        val stepImage = file("`stepImage\${idx}`", false, alpineJs = true)
        fun stepImage(idx: Int): String = "stepImage$idx"

        val stepDescription = TextArea("`steps[\${idx}].description`", true, alpineJs = true)
        fun stepsDescription(idx: Int): String = "steps[$idx].description"

        val deleteStep = button("deleteStep", "Удалить шаг")
        val addStep = button("addStep", "Добавить шаг")
        val save = submit("save", value = "Сохранить")

        override val components = listOf(
            title,
            duration,
            exerciseType,
            description,

            stepImage,
            stepDescription,

            deleteStep,
            addStep,
            save
        )

    }

    override fun match(element: Element) {
        element.select("title")[0].text() shouldBe title

        element shouldHaveComponent CreateExerciseForm
    }

}