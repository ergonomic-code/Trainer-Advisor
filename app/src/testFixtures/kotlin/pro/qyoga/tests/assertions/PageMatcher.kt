package pro.qyoga.tests.assertions

import io.kotest.assertions.fail
import io.kotest.matchers.should
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import pro.qyoga.tests.platform.html.HtmlPage


@FunctionalInterface
fun interface PageMatcher {

    fun match(element: Element)

}

interface ComponentMatcher : PageMatcher {

    fun selector(): String

    companion object {
        operator fun invoke(selector: String, matcher: PageMatcher) = object : ComponentMatcher {
            override fun selector(): String = selector
            override fun match(element: Element) {
                matcher.match(element)
            }
        }
    }

}

infix fun Document.shouldBe(pageMatcher: PageMatcher) = pageMatcher.match(this)

infix fun Document.shouldBePage(pageMatcher: PageMatcher) = pageMatcher.match(this)

infix fun ByteArray.shouldBePage(page: HtmlPage) = Jsoup.parse(String(this)) should page.matcher

infix fun Element.shouldHave(componentMatcher: ComponentMatcher) {
    this should haveComponent(componentMatcher.selector())
    val el = this.selectFirst(componentMatcher.selector())!!

    el shouldBeElement componentMatcher
}

infix fun Document.shouldHave(pageMatcher: PageMatcher) = pageMatcher.match(this)

infix fun Document.shouldBeFragment(componentMatcher: ComponentMatcher) {
    val fragmentElements = this.select("html body").single().children()
    if (fragmentElements.size != 1) {
        fail("Single document root element expected, but found ${fragmentElements.size}")
    }
    if (!fragmentElements.`is`(componentMatcher.selector())) {
        fail("${fragmentElements.single().descr} does not match ${componentMatcher.selector()}")
    }

    fragmentElements.single() shouldBeElement componentMatcher
}

infix fun Element.shouldBeElement(pageMatcher: PageMatcher) = pageMatcher.match(this)
