package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.haveElements


class ComboBox(baseName: String, override val required: Boolean, val items: List<ComboBoxItem>? = null) : InputBase {

    override val name = baseName

    val titleInputId = baseName + "Title"

    private val valueInput = Input(name, false, "hidden")

    val titleInput = Input.text(titleInputId, required, id = titleInputId)

    companion object {

        val itemsSelector = "ul.combo-box-search-result li"

        fun itemFor(comboBoxItem: ComboBoxItem): Component = object : Component {

            override fun selector(): String = "li button[data-item-id='${comboBoxItem.id}']"

            override fun matcher(): Matcher<Element> {
                val matchersList = buildList {

                    add(haveComponent("div:contains(${comboBoxItem.title.replace("'", "\\'")})"))
                    val details = comboBoxItem.details
                    if (details != null) {
                        add(haveComponent("small:contains(${details.replace("'", "\\'")})"))
                    }
                }

                return Matcher.all(
                    *matchersList.toTypedArray()
                )
            }

        }

    }

    override fun value(element: Element): String =
        element.select("input[type=hidden]").`val`()

    override fun selector(): String = "div.combo-box-div:has(input[name='${name}'])"

    override fun matcher(): Matcher<Element> {
        val matchers = buildList {
            add(haveComponent(valueInput))
            add(haveComponent(titleInput))
            if (items != null) {
                add(haveElements(itemsSelector, items.size))
            }
        }

        return Matcher.all(
            *matchers.toTypedArray()
        )
    }

}