package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveAttribute
import pro.qyoga.tests.assertions.haveAttributeValueMatching
import pro.qyoga.tests.assertions.isTag

class Link(
    val id: String,
    val urlPattern: String,
    val text: String,
    val targetAttr: String = "href"
) : Component {

    private val urlRegex = urlPattern.replace("\\{.*?}".toRegex(), ".*")
        .replace("?", "\\?")

    constructor(id: String, page: HtmlPageCompat, text: String) : this(id, page.path, text)

    override fun selector(): String =
        buildString {
            append("a[id*=$id]")
            if (text.isNotEmpty()) {
                append(":contains($text)")
            }
        }

    override fun matcher(): Matcher<Element> {
        return Matcher.all(
            isTag("a"),
            haveAttribute(targetAttr),
            haveAttributeValueMatching(targetAttr, urlRegex.toRegex())
        )
    }

    companion object {

        fun hxGet(id: String, url: String, text: String) = Link(id, url, text, "hx-get")

        fun hxDelete(id: String, url: String, text: String) = Link(id, url, text, "hx-delete")

    }

}