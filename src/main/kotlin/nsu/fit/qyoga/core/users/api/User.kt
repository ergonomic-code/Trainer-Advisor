package nsu.fit.qyoga.core.users.api

import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
    val username: String,
    val passwordHash: String,
    val roles: List<Role>,
    val id: Long = 0
)
