package pro.qyoga.tests.assertions

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


@FunctionalInterface
interface PageMatcher {

    fun match(element: Element)

}

infix fun Document.shouldBe(pageMatcher: PageMatcher) = pageMatcher.match(this)

infix fun Document.shouldBePage(pageMatcher: PageMatcher) = pageMatcher.match(this)

infix fun ByteArray.shouldBePage(pageMatcher: PageMatcher) = pageMatcher.match(Jsoup.parse(String(this)))

infix fun Element.shouldHave(pageMatcher: PageMatcher) {
    pageMatcher.match(this)
}

infix fun Document.shouldHave(pageMatcher: PageMatcher) = pageMatcher.match(this)

