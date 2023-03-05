package nsu.fit.qyoga.core.users.internal

import nsu.fit.qyoga.core.users.api.AuthService
import nsu.fit.qyoga.core.users.api.BadCredentials
import nsu.fit.qyoga.core.users.api.LoginRequest
import nsu.fit.qyoga.core.users.api.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val usersRepo: UsersRepo,
    private val passwordEncoder: PasswordEncoder
) : AuthService {

    override fun login(request: LoginRequest): User {
        val user = usersRepo.findByUsername(request.username)

        if (user == null || !passwordEncoder.matches(request.password, user.passwordHash)) {
            throw BadCredentials()
        }

        return user
    }

}
