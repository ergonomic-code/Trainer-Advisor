package pro.qyoga.core.users.therapists

import pro.qyoga.tech.captcha.CaptchaAnswer
import java.util.*


data class RegisterTherapistRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val captchaAnswer: CaptchaAnswer
) {

    fun withCaptchaId(captchaId: UUID): RegisterTherapistRequest =
        copy(captchaAnswer = captchaAnswer.copy(captchaId = captchaId))

    val fullName: String = "$firstName $lastName"

}
