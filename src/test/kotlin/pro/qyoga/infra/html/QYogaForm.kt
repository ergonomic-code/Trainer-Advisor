package pro.qyoga.infra.html

import io.kotest.assertions.withClue
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import org.jsoup.nodes.Element
import pro.qyoga.assertions.shouldBeElement
import pro.qyoga.assertions.shouldHaveAttr


abstract class QYogaForm(
    val id: String,
    val action: FormAction,
) : Component {

    abstract val components: List<Component>

    override fun match(element: Element) {
        withClue("Invalid form action") {
            element shouldHaveAttr action
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