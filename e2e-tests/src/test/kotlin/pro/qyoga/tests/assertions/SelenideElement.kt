package pro.qyoga.tests.assertions

import com.codeborne.selenide.SelenideElement
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should as koshould
import io.kotest.matchers.shouldNot as koshouldNot


fun beEmptyInput() = Matcher { element: SelenideElement ->
    if (element.tagName !in setOf("input", "textarea", "select")) {
        return@Matcher MatcherResult(
            false,
            { "Element ${element.descr()} should be an input element but it is not" },
            { "Element ${element.descr()} should not be an input element" })
    }

    return@Matcher MatcherResult(
        element.`val`().isNullOrEmpty(),
        { "Element ${element.descr()} should be empty" },
        { "Element ${element.descr()} should not be empty" }
    )
}

fun SelenideElement.shouldBeEmptyInput() {
    this koshould beEmptyInput()
}

fun SelenideElement.shouldNotBeEmptyInput() {
    this koshouldNot beEmptyInput()
}

fun SelenideElement.descr() = "<${this.tagName} ${this.getAttribute("id")}/>"
