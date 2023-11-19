package pro.qyoga.infra.email.api


data class RegisteredUserNotification(
    val name: String,
    val email: String,
    val password: String
)
