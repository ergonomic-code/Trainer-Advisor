package pro.qyoga.tests.clients.pages.therapist.clients.card

import org.jsoup.nodes.Element
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.pages.therapist.clients.ClientPageTabsFragment

object EditClientPage {

    val path = "/therapist/clients/{id}/card"

    val title = ".*"

    fun pageFor(client: Client): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element shouldHave ClientPageTabsFragment
            element.getElementById(EditClientForm.id)!! shouldBeElement EditClientForm.clientForm(client)
        }

    }

}