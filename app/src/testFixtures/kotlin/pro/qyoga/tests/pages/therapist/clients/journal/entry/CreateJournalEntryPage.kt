package pro.qyoga.tests.pages.therapist.clients.journal.entry

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.pages.therapist.clients.ClientPageTabsFragment
import pro.qyoga.tests.platform.html.QYogaPage
import java.time.LocalDate


class CreateJournalEntryPage(
    private val client: Client,
    private val entryDate: LocalDate
) : QYogaPage {

    override val path = CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL

    override val title = client.fullName()

    override fun match(element: Element) {
        element shouldHaveComponent ClientPageTabsFragment
        element shouldHave CreateJournalEntryFragment(client.id, entryDate)
        ClientPageTabsFragment.mobileTabName(element) shouldBe "Журнал"
    }

}