package pro.qyoga.tests.infra.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveComponent


abstract class QYogaForm(
    val id: String,
    val action: FormAction,
    private val elementClass: String? = null
) : Component {

    abstract val components: List<Component>

    override fun selector(): String =
        if (id.isNotBlank()) {
            "form#$id"
        } else if (!elementClass.isNullOrEmpty()) {
            "form.$elementClass"
        } else {
            error("Now selector for $this")
        }

    override fun matcher(): Matcher<Element> {
        val componentMatchers = components.map { haveComponent(it) }
        return Matcher.all(
            action.matcher(),
            *componentMatchers.toTypedArray()
        )
    }

}