package pro.qyoga.tests.clients.pages.therapist.clients

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.pages.therapist.clients.card.EditClientPage
import pro.qyoga.tests.clients.pages.therapist.clients.files.ClientFilesPage
import pro.qyoga.tests.clients.pages.therapist.clients.journal.list.EmptyClientJournalPage
import pro.qyoga.tests.infra.html.Component
import pro.qyoga.tests.infra.html.Link


object ClientPageTabsFragment : Component {

    private val journalLink = Link("journalLink", EmptyClientJournalPage.path, "Журнал")
    private val cardLink = Link("cardLink", EditClientPage.PATH, "Карточка")
    private val filesLink = Link("filesLink", ClientFilesPage.path, "Файлы")

    override val name = "clientTabs"

    override fun selector() = "#cardTabs"

    override fun match(element: Element) {
        element shouldHaveComponent journalLink
        element shouldHaveComponent cardLink
        element shouldHaveComponent filesLink
    }

}