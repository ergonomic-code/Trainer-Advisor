package pro.qyoga.tests.infra.html

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveAttribute

interface InputBase : Component {

    val required: Boolean

    fun requiredMatcher() = if (required) {
        haveAttribute("required")
    } else {
        haveAttribute("required").invert()
    }

    fun value(element: Element): String

}