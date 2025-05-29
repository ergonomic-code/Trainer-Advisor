package pro.qyoga.tests.pages.therapist.clients.journal.list

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.pages.therapist.clients.ClientPageTabsFragment
import pro.qyoga.tests.platform.html.Link
import pro.qyoga.tests.platform.html.QYogaPage
import java.util.*


abstract class ClientJournalPage(
    val clientId: UUID,
    private val addEntryLink: Link
) : QYogaPage {

    override val path = "/therapist/clients/{id}/journal"

    override val title = ""

    override fun match(element: Element) {
        element shouldHaveComponent ClientPageTabsFragment
        element shouldHaveComponent addEntryLink
        addEntryLinkClientId(element) shouldBe clientId
        ClientPageTabsFragment.journalLinkClientId(element) shouldBe clientId
        ClientPageTabsFragment.cardLinkClientId(element) shouldBe clientId
        ClientPageTabsFragment.filesLinkClientId(element) shouldBe clientId
    }

    private fun addEntryLinkClientId(element: Element): UUID? {
        return addEntryLink.pathParam(element, "clientId")
            ?.let { UUID.fromString(it) }
    }

}

class EmptyClientJournalPage(clientId: UUID) : ClientJournalPage(
    clientId,
    Link.hxGet("addFirstEntryLink", CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL, "Добавьте первую")
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
    Link.hxGet("addEntryLink", CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL, "Добавить запись")
) {

    override fun match(element: Element) {
        super.match(element)
        element shouldHave ClientJournalFragment.fragmentFor(entries, hasMore)
    }


}