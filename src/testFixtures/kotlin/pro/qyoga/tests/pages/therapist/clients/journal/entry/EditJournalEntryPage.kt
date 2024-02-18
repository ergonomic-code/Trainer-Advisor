package pro.qyoga.tests.pages.therapist.clients.journal.entry

import io.kotest.matchers.Matcher
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.alwaysSuccess
import pro.qyoga.tests.assertions.shouldBeComponent
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryForm
import pro.qyoga.tests.platform.html.Component


object EditJournalEntryPage : Component {

    const val PATH = "/therapist/clients/{clientId}/journal/{entryId}"

    override fun selector() = "#journalEntryTabContent"

    override fun matcher(): Matcher<Element> = alwaysSuccess()

    fun pageFor(entry: JournalEntry): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element.getElementById(EditJournalEntryForm.id)!! shouldBeComponent EditJournalEntryForm

            EditJournalEntryForm.dateInput.value(element) shouldBe russianDateFormat.format(entry.date)
            EditJournalEntryForm.therapeuticTaskNameInput.value(element) shouldBe entry.therapeuticTask.resolveOrThrow().name
            EditJournalEntryForm.entryTextInput.value(element) shouldBe entry.entryText
        }

    }

}