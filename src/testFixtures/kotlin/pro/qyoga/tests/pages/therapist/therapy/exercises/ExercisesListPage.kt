package pro.qyoga.tests.pages.therapist.therapy.exercises

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.model.ExerciseType
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.platform.html.*
import pro.qyoga.tests.platform.html.FormAction.Companion.hxGet
import pro.qyoga.tests.platform.html.Input.Companion.text


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

    private val newExerciseButton = Link("newExerciseLink", CreateExercisePage, "Добавить")

    val exercisePath = "$path/{exerciseId}"
    val updateAction = exercisePath
    val updateActionPattern = updateAction.replace("{exerciseId}", "(\\d+)").toRegex()

    val deleteAction = exercisePath
    val deleteActionPattern = updateAction.replace("{exerciseId}", "(\\d+)").toRegex()

    override fun match(element: Element) {
        element.select("title")[0].text() shouldBe title

        element shouldHaveComponent ExercisesSearchForm
        element shouldHaveComponent newExerciseButton
    }

    fun exercisesRows(document: Document): Elements =
        document.select(EXERCISE_ROW)

    fun exerciseRow(exercise: ExerciseSummaryDto): PageMatcher = object : PageMatcher {
        override fun match(element: Element) {
            element.select(EXERCISE_ROW).forAny { row ->
                row.select("td:nth-child(1)").text() shouldBe exercise.title
                row.select("td a.updateLink").attr("href") shouldMatch updateActionPattern
                row.select("td button.deleteButton").attr("hx-delete") shouldMatch deleteActionPattern
            }
        }
    }

}