package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveComponent
import kotlin.reflect.KProperty


abstract class QYogaForm(
    val id: String,
    val action: FormAction,
    private val elementClass: String? = null
) : Component {

    open val components: List<Component> = arrayListOf()

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


    fun <T : Component> component(factory: () -> T): ComponentDelegate<T> {
        val comp = factory()
        (this@QYogaForm.components as ArrayList).add(comp)
        return ComponentDelegate(comp)
    }

    inner class ComponentDelegate<T : Component>(private val comp: T) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return comp
        }

    }

}