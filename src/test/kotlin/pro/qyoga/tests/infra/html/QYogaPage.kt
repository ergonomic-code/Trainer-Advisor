package pro.qyoga.tests.infra.html

import pro.qyoga.tests.assertions.PageMatcher


interface QYogaPage : PageMatcher {

    val path: String
    val title: String

}