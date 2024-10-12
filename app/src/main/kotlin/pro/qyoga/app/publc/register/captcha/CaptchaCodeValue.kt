package pro.qyoga.app.publc.register.captcha

import java.time.Instant

data class CaptchaCodeValue(
        val captchaCode: String,
        val timestamp: Instant
)