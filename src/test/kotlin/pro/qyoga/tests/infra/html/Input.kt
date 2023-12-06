package pro.qyoga.tests.infra.html

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag

data class Input(
    override val name: String,
    override val required: Boolean,
    val type: String,
    val alpineJs: Boolean = false,
    val value: String? = null
) : InputBase {

    private val nameAttrName = if (alpineJs) ":name" else "name"

    override fun selector() = "input[$nameAttrName=$name][type=$type]"

    override fun match(element: Element) {
        element.tag() shouldBe Tag.valueOf("input")
        element.attr(nameAttrName) shouldBe name
        element.attr("type") shouldBe type
        if (value != null) {
            element.attr("value") shouldBe value
        }
        matchRequired(element)
    }

    companion object {

        fun text(name: String, required: Boolean, value: String? = null) =
            Input(name, required, "text", value = value)

        fun email(name: String, required: Boolean) =
            Input(name, required, "email")

        fun password(name: String, required: Boolean) =
            Input(name, required, "password")

        fun number(name: String, required: Boolean) =
            Input(name, required, "number")

        fun date(name: String, required: Boolean) =
            Input(name, required, "date")

        fun tel(name: String, required: Boolean) =
            Input(name, required, "tel")

        fun hidden(name: String, required: Boolean) =
            Input(name, required, "hidden")

        fun file(name: String, required: Boolean, alpineJs: Boolean = false) =
            Input(name, required, "file", alpineJs)

        fun button(name: String, value: String? = null) =
            Input(name, false, "button", value = value)

        fun submit(name: String, value: String? = null) =
            Input(name, false, "submit", value = value)

    }

}