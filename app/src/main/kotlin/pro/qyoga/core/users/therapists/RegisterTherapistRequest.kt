package pro.qyoga.core.users.therapists

import pro.qyoga.tech.captcha.CaptchaAnswer
import java.util.*


data class RegisterTherapistRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val captchaAnswer: CaptchaAnswer
) {

    fun withCaptchaHash(captchaHash: UUID): RegisterTherapistRequest =
        copy(captchaAnswer = captchaAnswer.copy(captchaId = captchaHash))

    val fullName: String = "$firstName $lastName"

}
