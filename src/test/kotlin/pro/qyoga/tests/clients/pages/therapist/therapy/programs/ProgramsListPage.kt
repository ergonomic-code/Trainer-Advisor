package pro.qyoga.tests.clients.pages.therapist.therapy.programs

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.Button
import pro.qyoga.tests.infra.html.FormAction
import pro.qyoga.tests.infra.html.Link
import pro.qyoga.tests.infra.html.QYogaPage


object ProgramsListPage : QYogaPage {

    override val path = "/therapist/programs"

    val programPath = "$path/{programId}"

    val programDocxPath = "$programPath/docx"

    override val title = "Программы"

    private val createProgramLink = Link("createProgramLink", CreateProgramPage, "Создать новую")

    fun editProgramLink(program: Program) = Link("editProgram${program.id}", programPath, program.title)


    fun downloadProgramDocxButton(programId: Long) = Link("downloadProgramDocx${programId}", programDocxPath, "")

    fun deleteProgramButton(program: Program) =
        Button("deleteProgram${program.id}", "", FormAction.hxDelete(programPath))

    private const val PROGRAM_ROW = "tbody tr"

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element shouldHaveComponent createProgramLink
    }

    fun programRows(document: Document): Elements {
        return document.select(PROGRAM_ROW)
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
