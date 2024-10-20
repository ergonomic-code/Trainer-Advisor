package pro.qyoga.tech.captcha

import java.time.Instant

data class CaptchaCodeValue(
        val captchaCode: String,
        val timestamp: Instant
)