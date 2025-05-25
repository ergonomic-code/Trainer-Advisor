package pro.qyoga.tests.pages.therapist.clients.journal.entry

import io.kotest.matchers.Matcher
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.alwaysSuccess
import pro.qyoga.tests.assertions.shouldBeComponent
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.pages.therapist.clients.journal.entry.JournalEntryForm.FormDraftScript
import pro.qyoga.tests.platform.html.Component
import java.util.*


object EditJournalEntryPage : Component {

    const val PATH = "/therapist/clients/{clientId}/journal/{entryId}"

    override fun selector() = "#journalEntryTabContent"

    override fun matcher(): Matcher<Element> = alwaysSuccess()

    fun pageFor(entry: JournalEntry): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element.getElementById(EditJournalEntryForm.id)!! shouldBeComponent EditJournalEntryForm

            EditJournalEntryForm.dateInput.value(element) shouldBe entry.date.toString()
            EditJournalEntryForm.therapeuticTaskComboBox.value(element) shouldBe entry.therapeuticTask!!.id.toString()
            EditJournalEntryForm.therapeuticTaskComboBox.title(element) shouldBe entry.therapeuticTask.resolveOrThrow().name
            EditJournalEntryForm.entryTextInput.value(element) shouldBe entry.entryText
            val scriptElement = element.select(FormDraftScript.selector()).single()
            FormDraftScript.clientId.value(scriptElement, UUID::class) shouldBe entry.clientRef.id
            FormDraftScript.entryId.value(scriptElement, Long::class) shouldBe entry.id
            FormDraftScript.entryDate.value(
                scriptElement,
                String::class
            ) shouldBe entry.date.toString()
            FormDraftScript.serverState.value(scriptElement, EditJournalEntryRq::class)!! shouldMatch entry
        }

    }

}