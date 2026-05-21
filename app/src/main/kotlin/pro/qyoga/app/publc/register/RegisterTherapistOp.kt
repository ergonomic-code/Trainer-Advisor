package pro.qyoga.app.publc.register

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pro.azhidkov.platform.errors.DomainError
import pro.azhidkov.platform.kotlin.mapFailure
import pro.azhidkov.platform.secrets.SecretChars
import pro.qyoga.core.users.auth.errors.DuplicatedEmailException
import pro.qyoga.core.users.therapists.CreateTherapistUserOp
import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.i9ns.email.NewUserRegisteredMessageChannel
import pro.qyoga.i9ns.email.WelcomeMessageChannel
import pro.qyoga.tech.captcha.CaptchaService
import java.awt.image.BufferedImage
import java.util.*
import kotlin.random.Random

class RegistrationException(
    val newCaptcha: Pair<UUID, BufferedImage>,
    msg: String,
    val isDuplicatedEmail: Boolean = false,
    val isInvalidCaptcha: Boolean = false,
    errorCode: String,
    cause: Throwable? = null,
) : DomainError(msg, cause, errorCode) {

    companion object {

        fun invalidCaptcha(newCaptcha: Pair<UUID, BufferedImage>) =
            RegistrationException(
                newCaptcha,
                "Invalid captcha code",
                isInvalidCaptcha = true,
                errorCode = "invalid-captcha"
            )

        fun duplicatedEmail(email: String, cause: DuplicatedEmailException, newCaptcha: Pair<UUID, BufferedImage>) =
            RegistrationException(
                newCaptcha,
                "User with email $email already exists",
                isDuplicatedEmail = true,
                errorCode = "duplicated-email",
                cause = cause
            )

    }

}

@Component
class RegisterTherapistOp(
    private val createTherapistUser: CreateTherapistUserOp,
    private val captchaService: CaptchaService,
    private val welcomeMessageChannel: WelcomeMessageChannel,
    private val newUserRegisteredMessageChannel: NewUserRegisteredMessageChannel,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    operator fun invoke(registerTherapistRequest: RegisterTherapistRequest): Therapist {
        if (captchaService.isInvalid(registerTherapistRequest.captchaAnswer)) {
            log.info("Register therapist with invalid captcha request terminated")
            throw RegistrationException.invalidCaptcha(newCaptcha = captchaService.generateCaptcha())
        }

        log.info("Registering new therapist: {}", registerTherapistRequest)

        val password = generateRandomPassword()
        val therapist = runCatching { createTherapistUser(registerTherapistRequest, password) }
            .mapFailure<DuplicatedEmailException, _> { ex ->
                RegistrationException.duplicatedEmail(
                    email = registerTherapistRequest.email,
                    cause = ex,
                    newCaptcha = captchaService.generateCaptcha()
                )
            }
            .getOrThrow()

        welcomeMessageChannel.sendWelcomeMessage(
            therapistEmail = registerTherapistRequest.email,
            password = password
        )
        newUserRegisteredMessageChannel.sendNewUserRegisteredMessage(
            therapistEmail = registerTherapistRequest.email
        )

        log.info("Therapist for {} registered", registerTherapistRequest.email)

        return therapist
    }

}

private val passwordChars = ('a'..'z').toList() + ('A'..'Z').toList() + ('0'..'9').toList()

private const val PASSWORD_LENGTH = 8

private fun generateRandomPassword() =
    buildString {
        repeat(PASSWORD_LENGTH) {
            append(passwordChars[Random.nextInt(passwordChars.size)])
        }
    }
        .let { SecretChars(it.toCharArray()) }
