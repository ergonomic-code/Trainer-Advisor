package pro.qyoga.tests.pages.therapist.therapy.programs

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.platform.html.*


object ProgramsListPage : QYogaPage {

    override val path = "/therapist/programs"

    val programPath = "$path/{programId}"

    val programDocxPath = "$programPath/docx"

    val programsSearchPath = "$path/search"

    override val title = "Программы"

    private val createProgramLink = Link("createProgramLink", CreateProgramPage, "Создать новую")

    object SearchForm : QYogaForm("programSearchForm", FormAction.hxGet(programsSearchPath)) {

        val titleKeywordInput = Input.text("titleKeyword", false)
        val therapeuticTaskKeywordInput = Input.text("therapeuticTaskKeyword", false)
        val searchButton = Button("searchButton", "")

        override val components = listOf(
            titleKeywordInput,
            therapeuticTaskKeywordInput,
            searchButton
        )

    }

    fun editProgramLink(program: Program) = Link("editProgram${program.id}", programPath, program.title)

    fun downloadProgramDocxButton(programId: Long) = Link("downloadProgramDocx${programId}", programDocxPath, "")

    fun deleteProgramButton(program: Program) =
        Button("deleteProgram${program.id}", "", FormAction.hxDelete(programPath))

    private const val PROGRAM_ROW = "tbody tr"

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element shouldHaveComponent SearchForm
        element shouldHaveComponent createProgramLink
    }

    fun rowsFor(programs: Iterable<Program>): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            val rows = element.select(PROGRAM_ROW)
            rows shouldHaveSize programs.toList().size
            (programs zip rows).forEach { (program, rowElement) ->
                rowElement.shouldBeRowFor(program)
            }
        }

    }

}

private infix fun Element.shouldBeRowFor(program: Program) {
    this shouldHaveComponent ProgramsListPage.downloadProgramDocxButton(program.id)
    this shouldHaveComponent ProgramsListPage.editProgramLink(program)
    this shouldHaveComponent ProgramsListPage.deleteProgramButton(program)
}
