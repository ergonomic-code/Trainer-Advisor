package pro.qyoga.infra.html

import io.kotest.assertions.withClue
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.assertions.shouldBeElement


abstract class QYogaForm(
    val id: String,
    val action: FormAction,
) : Component {

    abstract val components: List<Component>

    override fun match(element: Element) {
        withClue("Invalid form action") {
            withClue("Form has no '${action.attr}' attribute") {
                element.hasAttr(action.attr) shouldBe true
            }
            element.attr(action.attr) shouldBe action.url
        }

        components.forAll {
            withClue("Cannot find component ${it.name} by selector ${it.selector()} in $element") {
                element.select(it.selector()) shouldHaveSize 1
            }
            element.select(it.selector())[0] shouldBeElement it
        }
    }

    override fun selector(): String =
        "form#$id"

}