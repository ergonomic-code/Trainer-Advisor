package pro.qyoga.tests.infra.html

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldHaveSize
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.PageMatcher


interface Component : PageMatcher {

    val name
        get() = this::class.simpleName

    fun selector(): String

    override fun match(element: Element) {
        withClue("Cannot find element by ${selector()} in $element") {
            element.select(selector()) shouldHaveSize 1
        }
    }

}