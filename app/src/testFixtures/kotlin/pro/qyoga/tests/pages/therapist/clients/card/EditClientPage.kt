package pro.qyoga.tests.pages.therapist.clients.card

import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrNull
import pro.qyoga.app.therapist.clients.cards.EditClientCardPageController
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.pages.therapist.clients.ClientPageTabsFragment
import pro.qyoga.tests.platform.html.HtmlPageCompat

object EditClientPage {

    const val PATH = EditClientCardPageController.EDIT_CLIENT_CARD_PAGE_PATH

    fun pageFor(client: Client): PageMatcher = PageMatcher { element ->
        element shouldHaveComponent ClientPageTabsFragment
        element.getElementById(EditClientForm.id)!! shouldBeElement EditClientForm.clientForm(client)
    }

    fun forClient(client: ClientRef): HtmlPageCompat = object : HtmlPageCompat {
        override val path: String =
            EditClientCardPageController.EDIT_CLIENT_CARD_PAGE_PATH.replace("{clientId}", client.id.toString())
        override val title: String? = client.resolveOrNull()?.fullName()
    }

}
