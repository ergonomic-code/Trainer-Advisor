package pro.qyoga.tests.platform.html.custom

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataBlock
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component


data class TherapeuticDataBlock(
    private val label: String,
    private val fields: List<TherapeuticDataField>
) : Component {

    constructor(therapeuticDataBlock: TherapeuticDataBlock) : this(
        label = therapeuticDataBlock.label,
        fields = therapeuticDataBlock.fields.map { TherapeuticDataField(it) }
    )

    override fun selector(): String = "fieldset:has(legend:contains(${label}))"

    override fun matcher(): Matcher<Element> {
        return Matcher.all(*fields.map { haveComponent(it) }.toTypedArray())
    }

}