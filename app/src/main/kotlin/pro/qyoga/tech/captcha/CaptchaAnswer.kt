package pro.qyoga.tech.captcha

import java.util.*

data class CaptchaAnswer(
    val captchaId: UUID,
    val captchaCode: String,
)