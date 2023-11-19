package pro.qyoga.assertions

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element

infix fun Element.shouldHave(selector: String) {
    withClue("Cannot find element by $selector") {
        this.select(selector) shouldHaveSize 1
    }
}

infix fun Element.shouldHaveTitle(title: String) {
    this.select("title").text() shouldBe title
}