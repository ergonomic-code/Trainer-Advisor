package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import pro.qyoga.tech.captcha.CaptchaCodeValue
import pro.qyoga.tech.captcha.CaptchaService
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@Component
class CaptchaBackgrounds(
    private val captchaService: CaptchaService
) {

    private val captchas: ConcurrentHashMap<UUID, CaptchaCodeValue> = run {
        val declaredField = CaptchaService::class.java.getDeclaredField("captchas")
        declaredField.trySetAccessible()
        @Suppress("UNCHECKED_CAST")
        ReflectionUtils.getField(
            declaredField,
            captchaService
        ) as ConcurrentHashMap<UUID, CaptchaCodeValue>
    }

    fun generateCaptcha(): Pair<UUID, String> {
        val (id) = captchaService.generateCaptcha()

        return id to captchas[id]!!.captchaCode
    }

    fun getCaptchaCode(captchaId: UUID): String {
        return captchas[captchaId]!!.captchaCode
    }

}