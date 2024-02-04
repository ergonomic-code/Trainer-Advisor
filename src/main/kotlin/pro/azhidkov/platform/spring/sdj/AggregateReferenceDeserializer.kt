package pro.azhidkov.platform.spring.sdj

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.azhidkov.platform.spring.sdj.erpo.hydration.AggregateReferenceTarget
import pro.azhidkov.platform.spring.sdj.erpo.hydration.Identifiable


class AggregateReferenceDeserializer : JsonDeserializer<AggregateReference<*, *>>(), ContextualDeserializer {

    private var type: JavaType? = null

    override fun deserialize(parser: JsonParser, context: DeserializationContext): AggregateReference<*, *>? {
        val node = parser.codec.readTree<JsonNode>(parser)
        val propertyNames = node.properties().map { it.key }
        return if (propertyNames == setOf("id")) {
            AggregateReference.to<Any, Any>(node.get("id").numberValue())
        } else if (propertyNames.size > 1) {
            AggregateReferenceTarget<Identifiable<Any>, Any>(context.readTreeAsValue(node, type))
        } else {
            null
        }
    }

    override fun createContextual(ctx: DeserializationContext, property: BeanProperty): JsonDeserializer<*> {
        // Это странный костыль который помог мне быстро завести десериализацию ProgramExercise
        // Но подозреваю, в каком-то более сложном случае он взоравётся
        if (type == null) {
            this.type = property.type.containedType(0)
        }
        return this

    }

}