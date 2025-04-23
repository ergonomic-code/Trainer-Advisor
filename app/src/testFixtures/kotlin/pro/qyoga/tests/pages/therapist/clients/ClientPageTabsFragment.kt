package pro.qyoga.tests.pages.therapist.clients

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.clients.journal.list.JournalPageController
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage
import pro.qyoga.tests.pages.therapist.clients.files.ClientFilesPage
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Link
import java.util.*


object ClientPageTabsFragment : Component {

    private val journalLink = Link("journalLink", JournalPageController.JOURNAL_PAGE_PATH, "Журнал")
    private val cardLink = Link("cardLink", EditClientPage.PATH, "Карточка")
    private val filesLink = Link("filesLink", ClientFilesPage.path, "Файлы")

    override val name = "clientTabs"

    override fun selector() = "#cardTabs"

    override fun matcher() = Matcher.all(
        haveComponent(journalLink),
        haveComponent(cardLink),
        haveComponent(filesLink)
    )

    fun mobileTabName(element: Element): String =
        element.select("#clientPageTab").first()?.text() ?: ""

    fun journalLinkClientId(element: Element): UUID? {
        return journalLink.pathParam(element, "clientId")?.let { UUID.fromString(it) }
    }

    fun cardLinkClientId(element: Element): UUID? {
        return cardLink.pathParam(element, "clientId")?.let { UUID.fromString(it) }
    }

    fun filesLinkClientId(element: Element): UUID? {
        return filesLink.pathParam(element, "clientId")?.let { UUID.fromString(it) }
    }

}