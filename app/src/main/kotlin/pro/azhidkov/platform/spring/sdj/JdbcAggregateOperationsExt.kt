package pro.azhidkov.platform.spring.sdj

import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import pro.azhidkov.platform.spring.sdj.query.QueryBuilder
import pro.azhidkov.platform.spring.sdj.query.query


inline fun <reified T> JdbcAggregateOperations.findOneBy(noinline body: QueryBuilder.() -> Unit): T? {
    val query = query(body)
    return this.findOne(query, T::class.java)
        .orElse(null)
}

inline fun <reified T> JdbcAggregateOperations.findAllBy(
    page: Pageable,
    noinline body: QueryBuilder.() -> Unit
): Iterable<T> {
    val query = query(body)
    return this.findAll(query, T::class.java, page)
}
