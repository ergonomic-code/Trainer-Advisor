package pro.qyoga.app.publc.register.captcha

import java.util.*

data class CaptchaAnswer(val captchaHash: UUID,
                         val captchaCode: String,
                         val captchaImage: String?)