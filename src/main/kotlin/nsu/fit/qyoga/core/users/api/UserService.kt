package nsu.fit.qyoga.core.users.api

interface UserService {
    fun findByUsernameServ(username: String): User?
}
