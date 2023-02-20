package nsu.fit.qyoga.core.users.api

data class LoginRequest(
    val username: String,
    val password: String
)