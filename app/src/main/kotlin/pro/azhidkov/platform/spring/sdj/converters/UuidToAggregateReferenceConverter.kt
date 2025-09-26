package pro.azhidkov.platform.spring.sdj.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.data.jdbc.core.mapping.AggregateReference
import java.util.*

object UuidToAggregateReferenceConverter : Converter<UUID, AggregateReference<*, UUID>> {
    override fun convert(source: UUID): AggregateReference<*, UUID> {
        return AggregateReference.to<Any, UUID>(source)
    }
}
