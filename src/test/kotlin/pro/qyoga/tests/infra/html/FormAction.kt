package pro.qyoga.tests.infra.html

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import io.kotest.matchers.string.shouldMatch
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.PageMatcher


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

    }

    override fun match(element: Element) {
        withClue(element) {
            element.tag().name shouldBe "form"
        }
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