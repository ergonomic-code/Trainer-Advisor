package pro.qyoga.tests.pages.therapist.clients.card

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldBeComponent
import pro.qyoga.tests.assertions.shouldHaveTitle
import pro.qyoga.tests.assertions.shouldNotHave
import pro.qyoga.tests.pages.therapist.clients.ClientPageTabsFragment
import pro.qyoga.tests.platform.html.QYogaPage

object CreateClientPage : QYogaPage {

    override val path = "/therapist/clients/create"

    override val title = "Новый клиент"

    override fun match(element: Element) {
        element shouldHaveTitle title
        element.getElementById(CreateClientForm.id)!! shouldBeComponent CreateClientForm
        element shouldNotHave ClientPageTabsFragment
    }

}