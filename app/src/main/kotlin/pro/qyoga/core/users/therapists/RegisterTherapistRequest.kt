package pro.qyoga.core.users.therapists

import pro.qyoga.app.publc.register.captcha.CaptchaAnswer


data class RegisterTherapistRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val captchaAnswer: CaptchaAnswer
) {

    val fullName: String = "$firstName $lastName"

}
