package pro.azhidkov.platform.spring.jdbc

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.RowMapper


inline fun <reified T> rowMapperFor(objectMapper: ObjectMapper, columnName: String? = null) = RowMapper<T> { rs, _ ->
    val json: String? = if (columnName != null) {
        rs.getString(columnName)
    } else {
        rs.getString(1)
    }
    json?.let { objectMapper.readValue(it, T::class.java) }
}