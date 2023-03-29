package nsu.fit.qyoga.core.users.internal

import nsu.fit.qyoga.core.users.api.User
import java.security.Principal

data class UserPrincipal(private val user: User) : Principal {
    fun getId(): Long {
        return user.id
    }

    override fun getName(): String {
        return user.username
    }
}
