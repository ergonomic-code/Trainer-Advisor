package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element


interface HtmlPageCompat {
    val path: String
    val title: String?
}

interface HtmlPage : HtmlPageCompat {
    val matcher: Matcher<Element>
}
