package pro.qyoga.i9ns.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pro.azhidkov.platform.secrets.SecretChars
import pro.qyoga.infra.email.Email
import pro.qyoga.infra.email.EmailSender


@Component
class WelcomeMessageChannel(
    private val emailSender: EmailSender,
    @Value("\${spring.mail.username}") private val fromEmail: String,
) {

    fun sendWelcomeMessage(
        therapistEmail: String,
        password: SecretChars
    ) {
        emailSender.sendEmail(welcomeEmail(therapistEmail, password.show()))
    }

    private fun welcomeEmail(to: String, password: String) =
        Email(
            fromEmail,
            to,
            "Добро пожаловать в Trainer Advisor",
            """
                Здравствуйте!
                
                Вы зарегистрировались в Trainer Advisor.
                Логин от вашего аккаунта: $to. 
                Пароль от вашего аккаунта: $password.
                
                Теперь вы можете войти в систему на странице https://trainer-advisor.pro/login используя email и пароль из этого письма.
                
                С уважением, команда Trainer Advisor.
            """.trimIndent()
        )

}
