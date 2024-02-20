package pro.qyoga.tests.assertions

import io.kotest.matchers.should
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import pro.qyoga.tests.platform.html.HtmlPage


@FunctionalInterface
fun interface PageMatcher {

    fun match(element: Element)

}

infix fun Document.shouldBe(pageMatcher: PageMatcher) = pageMatcher.match(this)

infix fun Document.shouldBePage(pageMatcher: PageMatcher) = pageMatcher.match(this)

infix fun ByteArray.shouldBePage(page: HtmlPage) = Jsoup.parse(String(this)) should page.matcher

infix fun Element.shouldHave(pageMatcher: PageMatcher) {
    pageMatcher.match(this)
}

infix fun Document.shouldHave(pageMatcher: PageMatcher) = pageMatcher.match(this)

infix fun Element.shouldBeElement(pageMatcher: PageMatcher) = pageMatcher.match(this)
