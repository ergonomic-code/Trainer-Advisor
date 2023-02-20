package nsu.fit.qyoga.core.users.internal

import nsu.fit.qyoga.core.users.api.Role
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
    val username: String,
    val passwordHash: String,
    val roles: List<Role>,
    val id: Long = 0
)
