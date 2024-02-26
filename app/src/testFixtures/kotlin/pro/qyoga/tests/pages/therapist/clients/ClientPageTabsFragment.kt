package pro.qyoga.tests.pages.therapist.clients

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage
import pro.qyoga.tests.pages.therapist.clients.files.ClientFilesPage
import pro.qyoga.tests.pages.therapist.clients.journal.list.EmptyClientJournalPage
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Link


object ClientPageTabsFragment : Component {

    private val journalLink = Link("journalLink", EmptyClientJournalPage.path, "Журнал")
    private val cardLink = Link("cardLink", EditClientPage.PATH, "Карточка")
    private val filesLink = Link("filesLink", ClientFilesPage.path, "Файлы")

    override val name = "clientTabs"

    override fun selector() = "#cardTabs"

    override fun matcher() = Matcher.all(
        haveComponent(journalLink),
        haveComponent(cardLink),
        haveComponent(filesLink)
    )

}