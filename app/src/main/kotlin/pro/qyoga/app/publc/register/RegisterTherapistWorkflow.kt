package pro.qyoga.app.publc.register

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pro.qyoga.core.users.therapists.CreateTherapistUserWorkflow
import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.i9ns.email.Email
import pro.qyoga.i9ns.email.EmailSender
import pro.qyoga.tech.captcha.CaptchaService
import pro.qyoga.tech.captcha.IncorrectCaptchaCodeException
import kotlin.random.Random

@Component
class RegisterTherapistWorkflow(
    private val createTherapistUser: CreateTherapistUserWorkflow,
    private val emailSender: EmailSender,
    private val captchaService: CaptchaService,
    @Value("\${spring.mail.username}") private val fromEmail: String,
    @Value("\${trainer-advisor.admin.email}") private val adminEmail: String
) : (RegisterTherapistRequest) -> Therapist {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun invoke(registerTherapistRequest: RegisterTherapistRequest): Therapist {
        log.info("Registering new therapist: {}", registerTherapistRequest)

        if (!captchaService.verifyCaptcha(
                registerTherapistRequest.captchaAnswer.captchaId,
                registerTherapistRequest.captchaAnswer.captchaCode
            )
        ) {
            throw IncorrectCaptchaCodeException()
        }

        val password = randomPassword()

        val therapist = createTherapistUser(registerTherapistRequest, password)

        emailSender.sendEmail(newTherapistPasswordEmail(to = registerTherapistRequest.email, password))
        emailSender.sendEmail(
            newRegistrationNotificationEmail(
                to = adminEmail,
                therapistEmail = registerTherapistRequest.email
            )
        )

        log.info("Therapist for {} registered", registerTherapistRequest.email)

        return therapist
    }

    private fun newTherapistPasswordEmail(to: String, password: String) =
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

    private fun newRegistrationNotificationEmail(to: String, therapistEmail: String) = Email(
        fromEmail,
        to,
        "Новый терапевт добавлен в систему",
        """
                Email: $therapistEmail
            """.trimIndent()
    )

}

private val passwordChars = ('a'..'z').toList() + ('A'..'Z').toList() + ('0'..'9').toList()

private const val PASSWORD_LENGTH = 8

private fun randomPassword() =
    buildString {
        repeat(PASSWORD_LENGTH) {
            append(passwordChars[Random.nextInt(passwordChars.size)])
        }
    }

