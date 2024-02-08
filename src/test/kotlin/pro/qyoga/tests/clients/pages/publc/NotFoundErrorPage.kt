package pro.qyoga.tests.clients.pages.publc

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.haveElements
import pro.qyoga.tests.assertions.haveTitle
import pro.qyoga.tests.infra.html.HtmlPage
import pro.qyoga.tests.infra.html.Link


object NotFoundErrorPage : HtmlPage {

    override val path = ""

    override val title = "Страница не существует"

    private val returnToMainLink = Link("toMainLink", "/", "Вернуться на главную")

    override val matcher: Matcher<Element> = Matcher.all(
        haveTitle(title),
        haveElements("#notFoundErrorMessage", 1),
        haveComponent(returnToMainLink)
    )

}