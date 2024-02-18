package pro.qyoga.tests.platform.html

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.htmlMatch
import pro.qyoga.tests.infra.test_config.spring.context


data class Variable(
    val name: String
) {

    fun regex(): Regex = "let $name = (.*?);.*".toRegex(RegexOption.DOT_MATCHES_ALL)

    fun <T> value(scriptElement: Element, typeReference: TypeReference<T>): T {
        val scriptText = scriptElement.html()
        val varValue = regex().matchEntire(scriptText)
        checkNotNull(varValue) { "Cannot extract value of $name in $scriptText" }
        return context.getBean(ObjectMapper::class.java).readValue(varValue.groupValues[1], typeReference)
    }

}

abstract class Script(
    val id: String
) : Component {

    override fun selector() = "#$id"

    abstract val vars: List<Variable>


    override fun matcher(): Matcher<Element> {
        val varMatchers = vars.map { htmlMatch(it.regex()) }
        return Matcher.all(*varMatchers.toTypedArray())
    }
}