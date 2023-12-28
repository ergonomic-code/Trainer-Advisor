package pro.qyoga.tests.infra.html

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import pro.qyoga.tests.assertions.shouldHave


data class Button(
    override val name: String,
    val text: String,
    val action: FormAction? = null
) : Component {

    override fun selector(): String =
        buildString {
            append("button[name=$name]")
            if (text.isNotEmpty()) {
                append(":contains($text)")
            }
        }

    override fun match(element: Element) {
        element.tag() shouldBe Tag.valueOf("button")
        element.attr("name") shouldBe name
        if (action != null) {
            element shouldHave action
        }
    }

}