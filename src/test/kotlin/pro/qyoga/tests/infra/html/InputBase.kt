package pro.qyoga.tests.infra.html

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element

interface InputBase : Component {

    val required: Boolean

    fun matchRequired(element: Element) {
        val actualRequired = element.hasAttr("required")
        withClue("Unexpected required value") {
            actualRequired shouldBe required
        }
    }
}