package pro.qyoga.tests.clients.pages.therapist.clients.journal.entry

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.formats.russianDateFormat
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.platform.spring.sdj.AggregateReferenceTarget
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.infra.html.Component


object EditJournalEntryPage : Component {

    const val PATH = "/therapist/clients/{clientId}/journal/{entryId}"

    override fun selector() = "#journalEntryTabContent"

    fun pageFor(entry: JournalEntry): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element.getElementById(EditJournalEntryForm.id)!! shouldBeElement EditJournalEntryForm

            EditJournalEntryForm.dateInput.value(element) shouldBe russianDateFormat.format(entry.date)
            EditJournalEntryForm.therapeuticTaskNameInput.value(element) shouldBe (entry.therapeuticTask as AggregateReferenceTarget<TherapeuticTask, Long>).entity.name
            EditJournalEntryForm.entryTextInput.value(element) shouldBe entry.entryText
        }

    }

}