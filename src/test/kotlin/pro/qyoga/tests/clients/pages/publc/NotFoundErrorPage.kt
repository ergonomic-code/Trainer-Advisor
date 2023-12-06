package pro.qyoga.tests.clients.pages.publc

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveTitle
import pro.qyoga.tests.infra.html.Link
import pro.qyoga.tests.infra.html.QYogaPage


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