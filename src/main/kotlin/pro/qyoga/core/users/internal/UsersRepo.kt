package pro.qyoga.core.users.internal

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.relational.core.query.Query.query
import org.springframework.data.relational.core.query.isEqual
import org.springframework.stereotype.Repository
import pro.qyoga.core.users.api.User
import pro.qyoga.platform.spring.sdj.where


@Repository
class UsersRepo(
    private val jdbcAggregateTemplate: JdbcAggregateOperations
) {

    fun findByUsername(username: String): User? {
        val query = query(where(User::email).isEqual(username))
        return jdbcAggregateTemplate.findOne(query, User::class.java).orElse(null)
    }

}