package pro.qyoga.clients.pages.publc

import org.jsoup.nodes.Element
import pro.qyoga.assertions.shouldHave
import pro.qyoga.assertions.shouldHaveTitle
import pro.qyoga.infra.html.Link
import pro.qyoga.infra.html.QYogaPage


object NotFoundErrorPage : QYogaPage {


    override val path = ""

    override val title = "Страница не существует"

    private val returnToMainLink = Link("/", "Вернуться на главную")

    override fun match(element: Element) {
        element shouldHaveTitle title
        element shouldHave "#notFoundErrorMessage"
        element shouldHave returnToMainLink
    }

}