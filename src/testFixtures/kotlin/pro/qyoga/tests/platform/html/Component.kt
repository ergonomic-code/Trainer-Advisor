package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element


interface Component {

    val name
        get() = this::class.simpleName

    fun selector(): String

    fun matcher(): Matcher<Element>

}