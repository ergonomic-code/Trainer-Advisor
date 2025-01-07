package pro.qyoga.tests.pages.therapist.clients.journal.entry

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.platform.html.QYogaPage
import java.util.*


class CreateJournalEntryPage(val clientId: UUID) : QYogaPage {

    override val path: String = CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL

    override val title: String = ""

    override fun match(element: Element) {
        element shouldHaveComponent CreateJournalEntryForm
        CreateJournalEntryForm.actionParam(element, "clientId")?.let { UUID.fromString(it) } shouldBe clientId
    }

}