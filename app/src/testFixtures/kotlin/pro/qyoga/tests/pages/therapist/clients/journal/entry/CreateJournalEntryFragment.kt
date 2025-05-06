package pro.qyoga.tests.pages.therapist.clients.journal.entry

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.platform.html.QYogaPage
import java.time.LocalDate
import java.util.*


class CreateJournalEntryFragment(val clientId: UUID, private val today: LocalDate) : QYogaPage {

    override val path: String = CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL

    override val title: String = ""

    override fun match(element: Element) {
        element shouldHaveComponent CreateJournalEntryForm
        CreateJournalEntryForm.actionParam(element, "clientId")?.let { UUID.fromString(it) } shouldBe clientId
        CreateJournalEntryForm.dateInput.value(element) shouldBe russianDateFormat.format(LocalDate.now())
        JournalEntryForm.FormDraftScript.clientId.value(
            element.select(JournalEntryForm.FormDraftScript.selector()).single(), UUID::class
        ) shouldBe clientId
        JournalEntryForm.FormDraftScript.entryDate.value(
            element.select(JournalEntryForm.FormDraftScript.selector()).single(), String::class
        ) shouldBe (today.format(russianDateFormat))
    }

}