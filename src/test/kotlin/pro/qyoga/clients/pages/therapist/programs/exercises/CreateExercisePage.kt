package pro.qyoga.clients.pages.therapist.programs.exercises

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.assertions.shouldHave
import pro.qyoga.infra.html.*
import pro.qyoga.infra.html.Input.Companion.button
import pro.qyoga.infra.html.Input.Companion.file
import pro.qyoga.infra.html.Input.Companion.number
import pro.qyoga.infra.html.Input.Companion.submit
import pro.qyoga.infra.html.Input.Companion.text


object CreateExercisePage : QYogaPage {

    override val path = "/therapist/exercises/create"

    override val title = "Новое упражнение"

    object CreateExerciseForm : QYogaForm("createExercise", FormAction.hxPost(path)) {
        val title = text("title")

        val duration = number("duration")
        val exerciseType = Select("exerciseType")
        val description = TextArea("description")

        val stepImage = file("`stepImage\${idx}`", alpineJs = true)
        fun stepImage(idx: Int): String = "stepImage$idx"

        val stepDescription = TextArea("`steps[\${idx}].description`", alpineJs = true)
        fun stepsDescription(idx: Int): String = "steps[$idx].description"

        val deleteStep = text("deleteStep", value = "Удалить шаг")
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

        element shouldHave CreateExerciseForm
    }

}