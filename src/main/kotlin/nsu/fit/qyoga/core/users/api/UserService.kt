package nsu.fit.qyoga.core.users.api

interface UserService {
    fun findByUsername(username: String): User?
}
