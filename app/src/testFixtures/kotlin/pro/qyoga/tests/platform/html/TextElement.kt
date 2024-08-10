package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveText
import pro.qyoga.tests.assertions.isTag
import pro.qyoga.tests.platform.kotest.buildAllOfMatcher

fun h3(id: String, text: String) = TextElement(
    "h3",
    "#$id",
    haveText(text)
)

fun div(id: String? = null, clazz: String? = null, text: String) = TextElement(
    "div",
    selector(id, clazz, "div"),
    haveText(text)
)

data class TextElement(
    val tag: String,
    val selector: String,
    val textMatcher: Matcher<Element>?
) : Component {

    override fun selector() = selector

    override fun matcher(): Matcher<Element> = buildAllOfMatcher {
        add(isTag(tag))
        if (textMatcher != null) {
            add(textMatcher)
        }
    }

}