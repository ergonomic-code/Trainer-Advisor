package pro.qyoga.infra.html

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import org.jsoup.nodes.Element
import pro.qyoga.assertions.PageMatcher


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

    }

    override fun match(element: Element) {
        withClue("Form doesn't has attribute '$attr'") {
            element.hasAttr(attr) shouldBe true
        }

        withClue("Form has invalid url") {
            element.attr(attr) shouldBe url
        }

        if (method != null) {
            withClue("Form has invalid method") {
                element.attr("method") shouldBeEqualIgnoringCase method
            }
        }
    }

}