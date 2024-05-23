package pro.qyoga.tests.platform.html.custom

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptor
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.kotest.buildAllOfMatcher


class TherapeuticDataFormPart(
    private val blocks: List<TherapeuticDataBlock>
) : Component {

    constructor(therapeuticDataDescriptor: TherapeuticDataDescriptor) :
            this(therapeuticDataDescriptor.blocks.map { TherapeuticDataBlock(it) })

    override fun selector(): String = "#therapeuticData"

    override fun matcher(): Matcher<Element> {
        return buildAllOfMatcher {
            blocks.map { add(haveComponent(it)) }
        }
    }

}