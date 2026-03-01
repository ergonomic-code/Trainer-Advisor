package pro.azhidkov.platform.spring.jdbc

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.RowMapper
import pro.azhidkov.platform.spring.sdj.converters.PGIntervalToDurationConverter
import pro.azhidkov.platform.spring.sdj.converters.StringToSecretChars
import pro.azhidkov.platform.spring.sdj.converters.UuidToAggregateReferenceConverter


inline fun <reified T : Any> rowMapperFor(objectMapper: ObjectMapper, columnName: String? = null) = RowMapper<T> { rs, _ ->
    val json: String? = if (columnName != null) {
        rs.getString(columnName)
    } else {
        rs.getString(1)
    }
    requireNotNull(json) { "Expected JSON value for ${T::class.java.simpleName}" }
    objectMapper.readValue(json, T::class.java)
}

inline fun <reified T : Any> taDataClassRowMapper() =
    DataClassRowMapper.newInstance(T::class.java, DefaultConversionService().apply {
        addConverter(PGIntervalToDurationConverter())
        addConverter(UuidToAggregateReferenceConverter)
        addConverter(StringToSecretChars())
    })
