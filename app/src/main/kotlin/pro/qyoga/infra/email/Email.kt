package pro.qyoga.infra.email

data class Email(
    val from: String,
    val to: String,
    val subject: String,
    val text: String
)
