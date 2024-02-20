package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveAttributeValue
import pro.qyoga.tests.assertions.isTag


data class TextArea(
    override val name: String,
    override val required: Boolean,
    val alpineJs: Boolean = false
) : InputBase {

    private val nameAttrName = if (alpineJs) ":name" else "name"

    override fun matcher(): Matcher<Element> {
        return Matcher.all(
            isTag("textarea"),
            haveAttributeValue(nameAttrName, name),
            requiredMatcher()
        )
    }

    override fun selector(): String = "textarea[$nameAttrName=$name]"

    override fun value(element: Element): String =
        element.select(selector()).text()

}
