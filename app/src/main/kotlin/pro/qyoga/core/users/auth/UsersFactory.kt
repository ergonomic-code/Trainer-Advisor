package pro.qyoga.core.users.auth

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import pro.qyoga.core.users.auth.model.Role
import pro.qyoga.core.users.auth.model.User


@Component
class UsersFactory(
    private val passwordEncoder: PasswordEncoder
) {

    fun createUser(email: String, plainPassword: CharSequence, roles: Set<Role>): User {
        val passwordHash = passwordEncoder.encode(plainPassword)
        return User(email, passwordHash, roles.toTypedArray(), enabled = true)
    }

}