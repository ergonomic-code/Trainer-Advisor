package pro.qyoga.infra.html

import pro.qyoga.assertions.PageMatcher


interface QYogaPage : PageMatcher {

    val path: String
    val title: String

}