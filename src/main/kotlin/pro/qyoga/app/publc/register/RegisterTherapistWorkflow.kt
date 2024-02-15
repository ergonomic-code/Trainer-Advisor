package pro.qyoga.app.publc.register

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import pro.qyoga.core.users.therapists.CreateTherapistUserWorkflow
import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.core.users.therapists.Therapist
import kotlin.random.Random

@Component
class RegisterTherapistWorkflow(
    private val createTherapistUser: CreateTherapistUserWorkflow,
    private val mailSender: JavaMailSender
) : (RegisterTherapistRequest) -> Therapist {

    override fun invoke(registerTherapistRequest: RegisterTherapistRequest): Therapist {
        val password = randomPassword()
        val therapist = createTherapistUser(registerTherapistRequest, password)
        sendNewRegistrationNotification(registerTherapistRequest, password)
        return therapist
    }

    fun sendNewRegistrationNotification(registerRequest: RegisterTherapistRequest, password: String) {
        val mail = mailSender.createMimeMessage().apply {
            MimeMessageHelper(this).apply {
                setFrom("qyogapro@yandex.ru")
                setTo("me@azhidkov.pro")
                setSubject("Новая регистрация в QYoga!")
                setText(
                    "Имя: ${registerRequest.fullName}, " +
                            "Email: ${registerRequest.email}, " +
                            "пароль: $password."
                )
            }
        }
        mailSender.send(mail)
    }

}

private val passwordChars = ('a'..'z').toList() + ('A'..'Z').toList() + ('0'..'9').toList()

private const val PASSWORD_LENGTH = 8

private fun randomPassword() =
    buildString {
        repeat(PASSWORD_LENGTH) {
            append(passwordChars[Random.nextInt(passwordChars.size)])
        }
    }
