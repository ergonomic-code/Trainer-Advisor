package pro.qyoga.tests.clients.pages.therapist.therapy.programs

import com.fasterxml.jackson.core.type.TypeReference
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jsoup.nodes.Element
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.*


private const val PROGRAM_FORM_SCRIPT = "programFormScript"

abstract class ProgramPage(
    override val path: String,
    override val title: String,
    private val programForm: QYogaForm,
) : QYogaPage {

    val searchExercisePath = "/therapist/programs/create/search-exercises"

    val searchKey = "searchKey"

    object ProgramFormScript : Script(PROGRAM_FORM_SCRIPT) {

        val initialExercises = Variable("initialExercises")

        override val vars = listOf(initialExercises)

    }

    override fun match(element: Element) {
        if (title.isNotEmpty()) {
            element.select("title").text() shouldBe title
        }

        element shouldHaveComponent programForm
        element shouldHaveComponent ProgramFormScript

        element shouldHave "script#$PROGRAM_FORM_SCRIPT"
        val programFormScript = element.getElementById(PROGRAM_FORM_SCRIPT)!!
        programFormScript.html() shouldContain "$searchExercisePath?$searchKey"
    }

}

object CreateProgramPage : ProgramPage(CreateProgramForm.action.url, "Новая программа", CreateProgramForm)

object EditProgramPage : ProgramPage(EditProgramForm.action.url, "", EditProgramForm) {

    fun pageFor(program: Program): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            this@EditProgramPage.match(element)
            element.select("title").text() shouldBe program.title

            element shouldHaveComponent formFor(program)
        }

    }

    fun formFor(program: Program): Component = object : Component {

        override fun selector() =
            EditProgramForm.selector()

        override fun match(element: Element) {
            element shouldBeElement EditProgramForm

            EditProgramForm.titleInput.value(element) shouldBe program.title
            EditProgramForm.therapeuticTaskInput.value(element) shouldBe program.therapeuticTaskRef.resolveOrThrow().name

            // Грязный хак - скрип логически является частью компонента формы, а самопальный фреймворк
            // тестирования хтмл-компонентов считает что компонент формы целиком содержится в form
            // Но если из-за ошибки валидации вернуть всю форму, то там будет скрипт с редекларацией переменных
            // на которую будет ругаться браузер
            // Теоретически при ошибке валидации можно вернуть не всю форму, а только ошибочное поле,
            // но там тоже была какая-то проблема...
            // Как-то так
            val scriptElement = element.previousElementSibling()
            val formExercises =
                ProgramFormScript.initialExercises.value(
                    scriptElement!!,
                    object : TypeReference<List<ExerciseSummaryDto>>() {})
            (formExercises zip program.exercises).forAll { (actual, e) ->
                actual shouldBe e.exerciseRef.resolveOrThrow().toSummaryDto()
            }
        }

    }

}