package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveAttribute
import pro.qyoga.tests.assertions.haveAttributeValueMatching
import pro.qyoga.tests.assertions.haveText
import pro.qyoga.tests.assertions.isTag
import pro.qyoga.tests.platform.kotest.all
import pro.qyoga.tests.platform.pathToRegex

class Link(
    val id: String,
    val urlPattern: String,
    val text: String,
    val targetAttr: String = "href"
) : Component {

    val urlRegex = urlPattern.pathToRegex().toRegex()

    constructor(id: String, page: HtmlPageCompat, text: String) : this(id, page.path, text)

    override fun selector(): String =
        buildString {
            append("a[id*=$id]")
        }

    override fun matcher(): Matcher<Element> {
        return Matcher.all(
            isTag("a"),
            haveAttribute(targetAttr),
            haveAttributeValueMatching(targetAttr, urlRegex),
            if (text.isNotEmpty()) haveText(text) else null
        )
    }

    fun pathParam(element: Element, paramName: String): String? {
        val actualUrl = element.select(selector()).attr(targetAttr)
        val paramIdx = "\\{.*?}".toRegex().findAll(urlPattern)
            .indexOfFirst { it.value == "{$paramName}" }
            .takeIf { it >= 0 }
            ?: return null

        return urlRegex.matchEntire(actualUrl)!!.groupValues[paramIdx + 1]
    }

    companion object {

        fun hxGet(id: String, url: String, text: String) = Link(id, url, text, "hx-get")

        fun hxDelete(id: String, url: String, text: String) = Link(id, url, text, "hx-delete")

    }

}