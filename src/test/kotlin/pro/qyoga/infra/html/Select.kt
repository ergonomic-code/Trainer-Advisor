package pro.qyoga.infra.html

import io.kotest.assertions.withClue
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag

data class Option(
    val value: String,
    val title: String
)

data class Select(
    override val name: String,
    val options: List<Option>? = null
) : Component {

    override fun match(element: Element) {
        element.tag() shouldBe Tag.valueOf("select")
        element.attr("name") shouldBe name

        options?.forAll {
            withClue("Cannot find option $it") {
                element.select("option[value=${it.value}]:contains(${it.title})") shouldHaveSize 1
            }
        }
    }

    override fun selector(): String = "select[name=$name]"

}