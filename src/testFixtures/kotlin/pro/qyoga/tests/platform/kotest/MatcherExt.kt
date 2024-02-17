package pro.qyoga.tests.platform.kotest

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all


fun <T> buildAllOfMatcher(addMatchers: MutableList<Matcher<T>>.() -> Unit): Matcher<T> {
    val matchers = ArrayList<Matcher<T>>()
    matchers.addMatchers()
    return Matcher.all(*matchers.toTypedArray())
}
