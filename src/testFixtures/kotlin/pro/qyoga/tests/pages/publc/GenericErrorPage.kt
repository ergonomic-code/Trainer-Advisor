package pro.qyoga.tests.pages.publc

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveTitle
import pro.qyoga.tests.platform.html.QYogaPage


object GenericErrorPage : QYogaPage {

    override val path = ""

    override val title = "Что-то пошло не так"

    override fun match(element: Element) {
        element shouldHaveTitle title
        element shouldHave "#errorMessage"
    }

}