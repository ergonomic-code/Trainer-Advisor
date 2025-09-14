package pro.azhidkov.platform.spring.jdbc

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.RowMapper
import pro.azhidkov.platform.spring.sdj.converters.PGIntervalToDurationConverter
import java.util.*


inline fun <reified T> rowMapperFor(objectMapper: ObjectMapper, columnName: String? = null) = RowMapper<T> { rs, _ ->
    val json: String? = if (columnName != null) {
        rs.getString(columnName)
    } else {
        rs.getString(1)
    }
    json?.let { objectMapper.readValue(it, T::class.java) }
}

object UuidToAggregateReferenceConverter : Converter<UUID, AggregateReference<*, UUID>> {
    override fun convert(source: UUID): AggregateReference<*, UUID> {
        return AggregateReference.to<Any, UUID>(source)
    }
}

inline fun <reified T> taDataClassRowMapper() = DataClassRowMapper.newInstance(T::class.java).apply {
    conversionService = DefaultConversionService().apply {
        addConverter(PGIntervalToDurationConverter())
        addConverter(UuidToAggregateReferenceConverter)
    }
}