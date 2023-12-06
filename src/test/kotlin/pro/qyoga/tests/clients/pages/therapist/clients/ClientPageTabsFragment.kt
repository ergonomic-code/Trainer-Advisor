package pro.qyoga.tests.clients.pages.therapist.clients

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.pages.therapist.clients.card.EditClientPage
import pro.qyoga.tests.clients.pages.therapist.clients.journal.EmptyClientJournalPage
import pro.qyoga.tests.infra.html.Component
import pro.qyoga.tests.infra.html.Link


object ClientPageTabsFragment : Component {

    val journalLink = Link("journalLink", EmptyClientJournalPage.path, "Журнал")
    val cardLink = Link("cardLink", EditClientPage.path, "Карточка")

    override val name = "clientTabs"

    override fun selector() = "#cardTabs"

    override fun match(element: Element) {
        element shouldHave journalLink
        element shouldHave cardLink

    }

}