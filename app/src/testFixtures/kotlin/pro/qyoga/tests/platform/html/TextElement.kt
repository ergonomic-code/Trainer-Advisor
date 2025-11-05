package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.should
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.ComponentMatcher
import pro.qyoga.tests.assertions.haveText
import pro.qyoga.tests.assertions.isTag
import pro.qyoga.tests.platform.kotest.buildAllOfMatcher

fun h3(id: String, text: String) = TextElement(
    "#$id",
    haveText(text),
    "h3",
)

fun div(id: String? = null, clazz: String? = null, text: String) = TextElement(
    selector(id, clazz, "div"),
    haveText(text),
    "div",
)

data class TextElement(
    val selector: String,
    private val textMatcher: Matcher<Element>? = null,
    private val tag: String? = null,
) : Component, ComponentMatcher {

    override fun selector() = selector

    override fun matcher(): Matcher<Element> = buildAllOfMatcher {
        if (tag != null) {
            add(isTag(tag))
        }
        if (textMatcher != null) {
            add(textMatcher)
        }
    }

    override fun match(element: Element) {
        element should matcher()
    }

    fun text(element: Element): String =
        element.select(selector())
            .single()
            .text()

}
