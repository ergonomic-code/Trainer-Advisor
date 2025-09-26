package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import org.springframework.web.util.UriTemplate
import pro.qyoga.tests.assertions.haveAttribute
import pro.qyoga.tests.assertions.haveAttributeValueMatching
import pro.qyoga.tests.assertions.haveText
import pro.qyoga.tests.assertions.isTag
import pro.qyoga.tests.platform.kotest.all

class Link(
    val id: String,
    val urlPattern: String,
    val text: String,
    val targetAttr: String = "href"
) : Component {

    val urlRegex = UriTemplate(urlPattern)

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
        val vars = urlRegex.match(actualUrl)
        return vars[paramName]
    }

    companion object {

        fun hxGet(id: String, url: String, text: String) = Link(id, url, text, "hx-get")

        fun hxDelete(id: String, url: String, text: String) = Link(id, url, text, "hx-delete")

    }

}
