package nsu.fit.platform.spring

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations

fun <T : Any> NamedParameterJdbcOperations.queryForPage(
    baseSql: String,
    filterParams: Map<String, Any?>,
    pageRequest: Pageable,
    rowMapper: RowMapper<T>
): PageImpl<T> {
    val dataQuery = """
        SELECT * FROM ($baseSql) data
        LIMIT :pageSize
        OFFSET :offset
    """.trimIndent()

    val data = this.query(
        dataQuery,
        filterParams + mapOf("pageSize" to pageRequest.pageSize, "offset" to pageRequest.offset),
        rowMapper
    )

    val count = this.queryForObject(
        "SELECT count(*) FROM ($baseSql) data",
        filterParams,
        Long::class.java
    )!!
    return PageImpl(data, pageRequest, count)
}
