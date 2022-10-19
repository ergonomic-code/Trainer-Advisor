package nsu.fit.qyoga.core.users.model

import org.springframework.data.relational.core.mapping.Table

enum class Role {

    ROLE_ANONYMOUS,
    ROLE_CLIENT,
    ROLE_THERAPIST,
    ROLE_ADMIN

}

@Table("users")
data class User(
    val username: String,
    val passwordHash: String,
    val roles: List<Role>,
    val id: Long = 0
)
