package nsu.fit.qyoga.core.users.api

interface AuthService {

    fun login(request: LoginRequest): User

}
