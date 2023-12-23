package pro.qyoga.tests.clients.pages.therapist.clients.journal.entry

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.Component


object CreateJournalEntryPage : Component {

    const val PATH = "/therapist/clients/{id}/journal/create"

    override fun selector() = "#journalEntryTabContent"

    override fun match(element: Element) {
        element shouldHaveComponent CreateJournalEntryForm
    }

}