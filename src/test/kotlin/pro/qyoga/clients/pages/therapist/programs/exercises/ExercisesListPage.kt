package pro.qyoga.clients.pages.therapist.programs.exercises

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.qyoga.assertions.PageMatcher
import pro.qyoga.assertions.shouldHave
import pro.qyoga.core.programs.exercises.api.ExerciseDto
import pro.qyoga.core.programs.exercises.api.ExerciseType
import pro.qyoga.infra.html.*
import pro.qyoga.infra.html.FormAction.Companion.hxGet
import pro.qyoga.infra.html.Input.Companion.text


object ExercisesListPage : QYogaPage {

    override val path = "/therapist/exercises"

    override val title = "Список упражнений"

    object ExercisesSearchForm : QYogaForm("exercisesSearch", action = hxGet("$path/search")) {

        val title = text("title")
        val exercisesType = Select("exerciseType", ExerciseType.entries.map { Option(it.name, it.label) })
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