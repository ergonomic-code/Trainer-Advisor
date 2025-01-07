package pro.qyoga.tests.pages.therapist.clients.card

import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.clients.cards.EditClientCardPageController
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.pages.therapist.clients.ClientPageTabsFragment

object EditClientPage {

    const val PATH = EditClientCardPageController.EDIT_CLIENT_CARD_PAGE_PATH

    fun pageFor(client: Client): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element shouldHaveComponent ClientPageTabsFragment
            element.getElementById(EditClientForm.id)!! shouldBeElement EditClientForm.clientForm(client)
        }

    }

}