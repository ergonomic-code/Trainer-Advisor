package pro.qyoga.clients.pages.publc

import org.jsoup.nodes.Element
import pro.qyoga.assertions.shouldHave
import pro.qyoga.assertions.shouldHaveTitle
import pro.qyoga.infra.html.QYogaPage


object GenericErrorPage : QYogaPage {

    override val path = ""

    override val title = "Что-то пошло не так"

    override fun match(element: Element) {
        element shouldHaveTitle title
        element shouldHave "#errorMessage"
    }

}