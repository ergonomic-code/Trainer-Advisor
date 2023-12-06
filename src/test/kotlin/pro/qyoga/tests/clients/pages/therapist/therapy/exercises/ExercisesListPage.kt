package pro.qyoga.tests.clients.pages.therapist.therapy.exercises

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.qyoga.core.therapy.exercises.api.ExerciseDto
import pro.qyoga.core.therapy.exercises.api.ExerciseType
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.infra.html.*
import pro.qyoga.tests.infra.html.FormAction.Companion.hxGet
import pro.qyoga.tests.infra.html.Input.Companion.text


object ExercisesListPage : QYogaPage {

    override val path = "/therapist/exercises"

    override val title = "Список упражнений"

    object ExercisesSearchForm : QYogaForm("exercisesSearch", action = hxGet("$path/search")) {

        val title = text("title", false)
        val exercisesType = Select("exerciseType", false, ExerciseType.entries.map { Option(it.name, it.label) })
        val searchButton = Button("search", "Поиск")

        override val components = listOf(
            title,
            exercisesType,
            searchButton
        )

    }

    private const val EXERCISE_ROW = "tbody tr"

    private val newExerciseButton = Button("newExercise", "Новое упражнение")

    override fun match(element: Element) {
        element.select("title")[0].text() shouldBe title

        element shouldHave ExercisesSearchForm
        element shouldHave newExerciseButton
    }

    fun exercisesRows(document: Document): Elements =
        document.select(EXERCISE_ROW)

    fun exerciseRow(exercise: ExerciseDto): PageMatcher = object : PageMatcher {
        override fun match(element: Element) {
            element.select(EXERCISE_ROW).forAny { row ->
                row.select("td:nth-child(1)").text() shouldBe exercise.title
                row.select("td:nth-child(2)").text() shouldBe exercise.durationLabel
                row.select("td:nth-child(3)").text() shouldBe exercise.type.label
            }
        }
    }

}