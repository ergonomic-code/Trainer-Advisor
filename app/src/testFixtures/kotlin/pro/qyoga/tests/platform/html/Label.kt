package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveAttributeValue
import pro.qyoga.tests.assertions.haveText
import pro.qyoga.tests.assertions.isTag
import pro.qyoga.tests.platform.kotest.buildAllOfMatcher


data class Label(
    val forInput: String,
    val label: String
) : Component {

    override fun selector(): String = "label[for=$forInput]"

    override fun matcher(): Matcher<Element> = buildAllOfMatcher {
        add(isTag("label"))
        add(haveAttributeValue("for", forInput))
        add(haveText(label))
    }

}