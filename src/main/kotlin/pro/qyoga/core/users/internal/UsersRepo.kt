package pro.qyoga.core.users.internal

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.relational.core.query.Query.query
import org.springframework.data.relational.core.query.isEqual
import pro.qyoga.core.users.api.User
import pro.qyoga.platform.spring.sdj.where


class UsersRepo(
    private val jdbcAggregateTemplate: JdbcAggregateTemplate
) {

    fun findByUsername(username: String): User? {
        val query = query(where(User::email).isEqual(username))
        return jdbcAggregateTemplate.findOne(query, User::class.java).orElse(null)
    }

}