package pro.qyoga.tests.pages.therapist.therapy.programs

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jsoup.nodes.Element
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.platform.html.*


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

        override fun matcher(): Matcher<Element> {
            return Matcher.all(
                beComponent(EditProgramForm),
                haveInputWithValue(EditProgramForm.titleInput, program.title),
                haveInputWithValue(
                    EditProgramForm.therapeuticTaskInput,
                    program.therapeuticTaskRef.resolveOrThrow().name
                ),
            )
        }

    }

}