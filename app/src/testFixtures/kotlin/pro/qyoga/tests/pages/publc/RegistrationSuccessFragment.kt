package pro.qyoga.tests.pages.publc

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.alwaysSuccess
import pro.qyoga.tests.platform.html.Component

object RegistrationSuccessFragment : Component {

    override fun selector() =
        "div#registrationSuccess"

    override fun matcher(): Matcher<Element> = alwaysSuccess()


}