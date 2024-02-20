package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveAttributeValue
import pro.qyoga.tests.assertions.isTag

data class Input(
    override val name: String,
    override val required: Boolean,
    val type: String,
    val alpineJs: Boolean = false,
    val value: String? = null,
    val id: String? = null
) : InputBase {

    private val nameAttrName = if (alpineJs) ":name" else "name"

    override fun selector() =
        if (id != null)
            "#$id"
        else
            "input[$nameAttrName=$name][type=$type]" + (value?.let { "[value=$it]" } ?: "")

    override fun matcher(): Matcher<Element> {
        val matchers = buildList {
            add(isTag("input"))
            add(haveAttributeValue(nameAttrName, name))
            add(haveAttributeValue("type", type))
            if (value != null) {
                add(haveAttributeValue("value", value))
            }
            add(requiredMatcher())
        }
        return Matcher.all(*matchers.toTypedArray())
    }

    override fun value(element: Element): String =
        if (type == "checkbox") {
            (element.select(selector()).attr("checked") != "").toString()
        } else {
            element.select(selector()).`val`()
        }

    companion object {

        fun text(name: String, required: Boolean, value: String? = null, id: String? = null) =
            Input(name, required, "text", value = value, id = id)

        fun email(name: String, required: Boolean) =
            Input(name, required, "email")

        fun password(name: String, required: Boolean) =
            Input(name, required, "password")

        fun number(name: String, required: Boolean) =
            Input(name, required, "number")

        fun date(name: String, required: Boolean) =
            Input(name, required, "date")

        fun time(name: String, required: Boolean) =
            Input(name, required, "time")

        fun dateTimeLocal(name: String, required: Boolean) =
            Input(name, required, "datetime-local")

        fun checkbox(name: String, required: Boolean, value: String? = null) =
            Input(name, required, "checkbox", value = value)

        fun radio(name: String, required: Boolean, value: String? = null) =
            Input(name, required, "radio", value = value)

        fun tel(name: String, required: Boolean) =
            Input(name, required, "tel")

        fun file(name: String, required: Boolean, alpineJs: Boolean = false) =
            Input(name, required, "file", alpineJs)

        fun button(name: String, value: String? = null) =
            Input(name, false, "button", value = value)

        fun submit(name: String, value: String? = null) =
            Input(name, false, "submit", value = value)

    }

}