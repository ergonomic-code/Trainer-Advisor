package pro.qyoga.tests.pages.therapist.therapy.exercises

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.azhidkov.platform.java.time.toDecimalMinutes
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldBeComponent
import pro.qyoga.tests.platform.html.*

abstract class ExerciseForm(action: FormAction) : QYogaForm("exerciseForm", action) {

    val title = Input.text("title", true)

    val duration = Input.number("duration", true)
    val type = Select("type", true)
    val description = TextArea("description", true)

    val stepImage = Input.file("`stepImage\${idx}`", false, alpineJs = true)
    fun stepImage(idx: Int): String = "stepImage$idx"

    val stepDescription = TextArea("`steps[\${idx}].description`", true, alpineJs = true)
    fun stepsDescription(idx: Int): String = "steps[$idx].description"

    val deleteStep = Button("deleteStep", "")
    val addStep = Input.button("addStep", "Добавить шаг")
    val save = Button("save", "Сохранить")

    override val components = listOf(
        title,
        duration,
        type,
        description,

        stepImage,
        stepDescription,

        deleteStep,
        addStep,
        save
    )

}

object CreateExerciseForm : ExerciseForm(FormAction.hxPost(CreateExercisePage.path))

object EditExerciseForm : ExerciseForm(FormAction.hxPut(EditExercisePage.PATH)) {

    const val IMAGE_PATH = "/therapist/exercises/{exerciseId}/step-images/{stepIdx}"

    fun exerciseForm(exercise: Exercise): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element shouldBeComponent EditExerciseForm
            element.select(title.selector()).`val`() shouldBe exercise.title
            element.select(type.selector()).`val`() shouldBe exercise.exerciseType.name
            element.select(duration.selector()).`val`() shouldBe exercise.duration.toDecimalMinutes().toString()
            element.select(description.selector()).`val`() shouldBe exercise.description
        }

    }

}
