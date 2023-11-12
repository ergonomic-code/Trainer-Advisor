package pro.qyoga.assertions

import io.kotest.matchers.collections.shouldHaveSize
import org.jsoup.nodes.Document

infix fun Document.shouldHave(selector: String) {
    this.select(selector) shouldHaveSize 1
}