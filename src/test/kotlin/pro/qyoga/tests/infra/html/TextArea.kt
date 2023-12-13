package pro.qyoga.tests.infra.html

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag


data class TextArea(
    override val name: String,
    override val required: Boolean,
    val alpineJs: Boolean = false
) : InputBase {

    private val nameAttrName = if (alpineJs) ":name" else "name"

    override fun match(element: Element) {
        element.tag() shouldBe Tag.valueOf("textarea")
        element.attr(nameAttrName) shouldBe name
        matchRequired(element)
    }

    override fun selector(): String = "textarea[$nameAttrName=$name]"

}
