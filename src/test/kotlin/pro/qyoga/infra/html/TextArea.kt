package pro.qyoga.infra.html

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag


data class TextArea(
    override val name: String,
    val alpineJs: Boolean = false
) : Component {

    private val nameAttrName = if (alpineJs) ":name" else "name"

    override fun match(element: Element) {
        element.tag() shouldBe Tag.valueOf("textarea")
        element.attr(nameAttrName) shouldBe name
    }

    override fun selector(): String = "textarea[$nameAttrName=$name]"

}
