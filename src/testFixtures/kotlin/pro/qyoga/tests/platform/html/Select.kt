package pro.qyoga.tests.platform.html

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.azhidkov.platform.kotlin.LabeledEnum
import pro.qyoga.tests.assertions.descr
import pro.qyoga.tests.assertions.haveAttributeValue
import pro.qyoga.tests.assertions.isTag

data class Option(
    val value: String,
    val title: String
) {

    companion object {

        fun of(enumValue: LabeledEnum): Option =
            Option(enumValue.name, enumValue.label)

    }

}

data class Select(
    override val name: String,
    override val required: Boolean,
    val options: List<Option>? = null
) : InputBase {

    fun haveOptions(options: List<Option>): Matcher<Element> = Matcher { element: Element ->
        val elements = element.children()

        val optionsMatchers: List<Matcher<Elements>> =
            options.map { o ->
                object : Matcher<Elements> {
                    override fun test(value: Elements): MatcherResult {
                        val optionElement = elements.find { o.value == it.`val`() }
                        if (optionElement == null) {
                            return MatcherResult(
                                false,
                                { "HTML have no element with value ${o.value}" },
                                { "HTML should not have element with value ${o.value}" }
                            )
                        }

                        return MatcherResult(
                            optionElement.text() == o.title,
                            { "Option ${optionElement.descr} have title [${optionElement.text()}] but [${o.title}] expected" },
                            { "Option ${optionElement.descr} should not have title [${optionElement.text()}]" }
                        )
                    }
                }
            }

        val results = optionsMatchers.map { it.test(elements) }

        MatcherResult(
            results.all { it.passed() },
            { results.filterNot { it.passed() }.joinToString(separator = "\n") { it.failureMessage() } },
            { results.filter { it.passed() }.joinToString(separator = "\n") { it.negatedFailureMessage() } },
        )
    }

    override fun matcher(): Matcher<Element> = Matcher { element: Element ->
        val matchers = buildList {
            add(isTag("select"))
            add(haveAttributeValue("name", name))
            add(requiredMatcher())

            if (options != null) {
                add(haveOptions(options))
            }
        }

        Matcher.all(*matchers.toTypedArray()).test(element)
    }

    override fun value(element: Element): String =
        element.select("${selector()} option[selected]").`val`()

    override fun selector(): String = "select[name=$name]"

}