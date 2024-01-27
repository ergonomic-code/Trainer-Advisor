package pro.qyoga.platform.spring.jdbc

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations

fun <T : Any> NamedParameterJdbcOperations.queryForPage(
    baseSql: String,
    filterParams: Map<String, Any?>,
    pageRequest: Pageable,
    rowMapper: RowMapper<T>
): Page<T> {
    val count = this.queryForObject(
        "SELECT count(*) FROM ($baseSql) data",
        filterParams,
        Long::class.java
    )!!
    if (count == 0L) {
        val emptyPage: List<T> = emptyList()
        return PageImpl(emptyPage, pageRequest, count)
    }

    val dataQuery = buildString {
        appendLine("SELECT * FROM($baseSql) data")
        if (pageRequest.sort.isSorted) {
            append("ORDER BY ")
            appendLine(pageRequest.sort.map { it.property + " " + it.direction.name }.joinToString(", "))
        }
        appendLine(
            """
        LIMIT :pageSize
        OFFSET :offset
        """
        )
    }
    val data = this.query(
        dataQuery,
        filterParams + mapOf("pageSize" to pageRequest.pageSize, "offset" to pageRequest.offset),
        rowMapper
    )

    return PageImpl(data, pageRequest, count)
}
