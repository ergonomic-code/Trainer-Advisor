package pro.qyoga.i9ns.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pro.qyoga.infra.email.Email
import pro.qyoga.infra.email.EmailSender


@Component
class NewUserRegisteredMessageChannel(
    private val emailSender: EmailSender,
    @Value("\${spring.mail.username}") private val fromEmail: String,
    @Value("\${trainer-advisor.admin.email}") private val adminEmail: String
) {

    fun sendNewUserRegisteredMessage(therapistEmail: String) {
        emailSender.sendEmail(newRegistrationEmail(adminEmail, therapistEmail))
    }

    private fun newRegistrationEmail(to: String, therapistEmail: String) = Email(
        from = fromEmail,
        to = to,
        subject = "Новый терапевт добавлен в систему",
        text = "Email: $therapistEmail"
    )

}
