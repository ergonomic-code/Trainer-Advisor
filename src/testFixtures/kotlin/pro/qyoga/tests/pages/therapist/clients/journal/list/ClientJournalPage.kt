package pro.qyoga.tests.pages.therapist.clients.journal.list

import org.jsoup.nodes.Element
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.pages.therapist.clients.ClientPageTabsFragment
import pro.qyoga.tests.platform.html.QYogaPage


abstract class ClientJournalPage : QYogaPage {

    override val path = "/therapist/clients/{id}/journal"

    override val title = ""

    override fun match(element: Element) {
        element shouldHaveComponent ClientPageTabsFragment
    }

}

object EmptyClientJournalPage : ClientJournalPage() {

    override fun match(element: Element) {
        element shouldHaveComponent EmptyClientJournalFragment
    }

}

class NonEmptyClientJournalPage(private val entries: List<JournalEntry>) : ClientJournalPage() {

    override fun match(element: Element) {
        element shouldHave ClientJournalFragment.fragmentFor(entries)
    }


}