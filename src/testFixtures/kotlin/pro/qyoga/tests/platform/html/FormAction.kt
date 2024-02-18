package pro.qyoga.tests.platform.html

import io.kotest.assertions.withClue
import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import io.kotest.matchers.string.shouldMatch
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.haveAttribute
import pro.qyoga.tests.assertions.haveAttributeValue
import pro.qyoga.tests.assertions.haveAttributeValueMatching


data class FormAction(
    val attr: String,
    val url: String,
    val method: String? = null
) : PageMatcher {

    companion object {

        fun classicPost(url: String) =
            FormAction("action", url, "POST")

        fun hxGet(url: String): FormAction =
            FormAction("hx-get", url)

        fun hxPost(url: String): FormAction =
            FormAction("hx-post", url)

        fun hxPut(url: String): FormAction =
            FormAction("hx-put", url)

        fun hxDelete(url: String): FormAction =
            FormAction("hx-delete", url)

    }

    fun matcher(): Matcher<Element> {
        val matchers = buildList {
            add(haveAttribute(attr))
            add(haveAttributeValueMatching(attr, url.replace("\\{.*}".toRegex(), ".*").toRegex()))
            if (method != null) {
                add(haveAttributeValue("method", method, ignoreCase = true))
            }
        }
        return Matcher.all(*matchers.toTypedArray())
    }

    override fun match(element: Element) {
        withClue("Form $element doesn't has attribute '$attr'") {
            element.hasAttr(attr) shouldBe true
        }

        withClue("Form has invalid url") {
            element.attr(attr) shouldMatch url.replace("\\{.*}".toRegex(), ".*")
        }

        if (method != null) {
            withClue("Form has invalid method") {
                element.attr("method") shouldBeEqualIgnoringCase method
            }
        }
    }

}