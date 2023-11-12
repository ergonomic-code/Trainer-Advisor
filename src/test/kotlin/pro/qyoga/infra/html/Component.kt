package pro.qyoga.infra.html

import pro.qyoga.assertions.PageMatcher


interface Component : PageMatcher {

    val name
        get() = this::class.simpleName

    fun selector(): String

}