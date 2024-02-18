package pro.qyoga.tests.pages.therapist.clients.files

import io.kotest.matchers.collections.shouldHaveSize
import org.jsoup.nodes.Element
import pro.azhidkov.platform.file_storage.api.FileMetaData
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.clients.files.model.ClientFile
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.pages.therapist.clients.ClientPageTabsFragment
import pro.qyoga.tests.platform.html.*
import java.time.ZoneId


object ClientFilesPage : QYogaPage {

    override val path = "/therapist/clients/{clientId}/files"

    val filePath = "$path/{fileId}"

    override val title = "Файлы"

    val fileRow = ".fileRow"

    fun downloadFileLink(file: FileMetaData) = Link("downloadFile${file.id}", "$path/{fileId}", file.name)

    object UploadFileForm : QYogaForm("addFileForm", FormAction.hxPost(path)) {

        val fileInput: Input = Input.file("newFile", true)

        override val components = listOf(fileInput)

    }

    override fun match(element: Element) {
        element shouldHaveComponent ClientPageTabsFragment
        element shouldHaveComponent UploadFileForm
    }

    fun rowFor(file: FileMetaData): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element shouldBeRowFor file
        }

    }

    fun rowsFor(filesPage: Iterable<ClientFile>): PageMatcher = object : PageMatcher {
        override fun match(element: Element) {
            val rows = element.select(fileRow)
            rows shouldHaveSize filesPage.toList().size
            (filesPage zip rows).forEach { (file, rowElement) ->
                rowElement.shouldBeRowFor(file.fileRef.resolveOrThrow())
            }
        }
    }

}

private infix fun Element.shouldBeRowFor(file: FileMetaData) {
    this shouldHaveComponent ClientFilesPage.downloadFileLink(file)
    this shouldHave "small:contains(${
        "Загружен ${
            russianDateFormat.format(
                file.createdAt.atZone(ZoneId.systemDefault()).toLocalDate()
            )
        })"
    }"
    this shouldHaveComponent Button("deleteClientFile${file.id}", "", FormAction.hxDelete(ClientFilesPage.filePath))
}
