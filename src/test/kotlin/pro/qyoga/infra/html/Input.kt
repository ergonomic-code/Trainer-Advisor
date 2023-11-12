package pro.qyoga.infra.html

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag

data class Input(
    override val name: String,
    val type: String,
    val alpineJs: Boolean = false,
    val value: String? = null
) : Component {

    private val nameAttrName = if (alpineJs) ":name" else "name"

    override fun selector() = "input[$nameAttrName=$name][type=$type]"

    override fun match(element: Element) {
        element.tag() shouldBe Tag.valueOf("input")
        element.attr(nameAttrName) shouldBe name
        element.attr("type") shouldBe type
        if (value != null) {
            element.attr("value") shouldBe value
        }
    }

    companion object {

        fun text(name: String, value: String? = null) =
            Input(name, "text", value = value)

        fun email(name: String) =
            Input(name, "email")

        fun password(name: String) =
            Input(name, "password")

        fun number(name: String) =
            Input(name, "number")

        fun file(name: String, alpineJs: Boolean = false) =
            Input(name, "file", alpineJs)

        fun button(name: String, value: String? = null) =
            Input(name, "button", value = value)

        fun submit(name: String, value: String? = null) =
            Input(name, "submit", value = value)

    }

}