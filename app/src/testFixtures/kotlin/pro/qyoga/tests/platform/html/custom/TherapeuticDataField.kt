package pro.qyoga.tests.platform.html.custom

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.azhidkov.platform.extensible_entity.descriptor.CustomFieldType
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Input
import pro.qyoga.tests.platform.html.Label
import pro.qyoga.tests.platform.kotest.buildAllOfMatcher


sealed interface TherapeuticDataField : Component {

    companion object {
        operator fun invoke(field: pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataField): TherapeuticDataField =
            when (field.type) {
                CustomFieldType.STRING -> TherapeuticDataStringField(field)
                else -> TODO()
            }
    }

}

data class TherapeuticDataStringField(
    private val field: pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataField
) : TherapeuticDataField {

    override fun selector(): String = "#customField-${field.id}"

    private val inputName = "customField.${field.id}"

    override fun matcher(): Matcher<Element> {
        val labelText = field.label + if (field.required) " *" else ""
        return buildAllOfMatcher {
            add(haveComponent(Input.text(inputName, field.required)))
            add(haveComponent(Label(inputName, labelText)))
        }
    }

}