package pro.azhidkov.platform.spring.sdj

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.relational.core.query.Query


fun <T : Any> JdbcAggregateOperations.findOneBy(query: Query, type: Class<T>): T? {
    return this.findOne(query, type)
        .orElse(null)
}
