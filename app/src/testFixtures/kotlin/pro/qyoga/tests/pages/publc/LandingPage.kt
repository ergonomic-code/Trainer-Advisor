package pro.qyoga.tests.pages.publc

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveTitle
import pro.qyoga.tests.platform.html.QYogaPage


object LandingPage : QYogaPage {

    override fun match(element: Element) {
        element shouldHaveTitle title
    }

    override val path = "/"

    override val title = "Trainer Advisor"

}