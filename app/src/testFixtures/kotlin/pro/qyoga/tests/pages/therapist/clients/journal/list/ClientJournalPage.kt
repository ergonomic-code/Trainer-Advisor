package pro.qyoga.tests.pages.therapist.clients.journal.list

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.pages.therapist.clients.ClientPageTabsFragment
import pro.qyoga.tests.platform.html.QYogaPage
import java.util.*


abstract class ClientJournalPage(
    val clientId: UUID,
) : QYogaPage {

    override val path = "/therapist/clients/{id}/journal"

    override val title = ""

    override fun match(element: Element) {
        element shouldHaveComponent ClientPageTabsFragment
        ClientPageTabsFragment.journalLinkClientId(element) shouldBe clientId
        ClientPageTabsFragment.cardLinkClientId(element) shouldBe clientId
        ClientPageTabsFragment.filesLinkClientId(element) shouldBe clientId
    }

}

class EmptyClientJournalPage(clientId: UUID) : ClientJournalPage(
    clientId,
) {

    override fun match(element: Element) {
        super.match(element)
        element shouldHaveComponent EmptyClientJournalFragment
    }

}

class NonEmptyClientJournalPage(
    clientId: UUID,
    private val entries: List<JournalEntry>,
    private val hasMore: Boolean = false
) : ClientJournalPage(
    clientId,
) {

    override fun match(element: Element) {
        super.match(element)
        element shouldHave ClientJournalFragment.fragmentFor(entries, hasMore)
    }

}