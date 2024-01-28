package pro.qyoga.tests.assertions

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldHaveSize
import org.jsoup.nodes.Element

infix fun Element.shouldHave(selector: String) {
    withClue("Cannot find element by $selector") {
        this.select(selector) shouldHaveSize 1
    }
}