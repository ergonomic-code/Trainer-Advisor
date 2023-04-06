package nsu.fit.qyoga.core.users.internal

import nsu.fit.qyoga.core.users.api.User
import nsu.fit.qyoga.core.users.api.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepo: UsersRepo
) : UserService {
    override fun findByUsernameServ(username: String): User? {
        return userRepo.findByUsername(username)
    }
}
