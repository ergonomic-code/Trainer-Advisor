package pro.azhidkov.platform.spring.sdj

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.azhidkov.platform.spring.sdj.ergo.hydration.AggregateReferenceTarget
import pro.azhidkov.platform.spring.sdj.ergo.hydration.Identifiable
import tools.jackson.core.JsonParser
import tools.jackson.databind.*
import tools.jackson.databind.type.TypeFactory
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure


class AggregateReferenceDeserializer(
    private var type: JavaType = TypeFactory.unknownType()
) : ValueDeserializer<AggregateReference<*, *>>() {

    override fun deserialize(parser: JsonParser, context: DeserializationContext): AggregateReference<*, *>? {
        val node = parser.readValueAsTree<JsonNode>()
        val propertyNames = node.properties().map { it.key }.toSet()
        return if (propertyNames == setOf(ID_FIELD_NAME)) {
            val idType = getIdType(type)
            AggregateReference.to<Any, Any>(context.readTreeAsValue(node.findValue(ID_FIELD_NAME), idType))
        } else if (propertyNames.size > 1) {
            AggregateReferenceTarget<Identifiable<Any>, Any>(context.readTreeAsValue(node, type))
        } else {
            null
        }
    }

    override fun createContextual(ctx: DeserializationContext, property: BeanProperty): ValueDeserializer<*> {
        return AggregateReferenceDeserializer(property.type.containedType(0))
    }

    companion object {
        const val ID_FIELD_NAME = "id"
    }

}

private fun getIdType(type: JavaType): Class<out Any> =
    type.rawClass.kotlin
        .memberProperties
        .first { it.name == AggregateReferenceDeserializer.ID_FIELD_NAME }
        .returnType
        .jvmErasure
        .java
