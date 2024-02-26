package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveAttributeValueMatching
import pro.qyoga.tests.assertions.isTag


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

    override fun matcher(): Matcher<Element> {
        val matchers = buildList {
            add(isTag("button"))
            add(haveAttributeValueMatching("name", name.toRegex()))
            if (action != null) {
                add(action.matcher())
            }
        }
        return Matcher.all(
            *matchers.toTypedArray()
        )
    }
}