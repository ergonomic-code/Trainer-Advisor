package pro.qyoga.tech.captcha

import net.logicsquad.nanocaptcha.image.ImageCaptcha
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class CaptchaService {

    private val captchas = ConcurrentHashMap<UUID, CaptchaCodeValue>()

    fun verifyCaptcha(id: UUID, captchaCode: String): Boolean {
        removeAllExpiredCaptchas()
        return captchas[id]?.captchaCode == captchaCode
    }

    fun generateCaptcha(): Pair<UUID, BufferedImage> {
        removeAllExpiredCaptchas()
        val imageCaptcha = ImageCaptcha.create()
        val hash = UUID.randomUUID()
        captchas[hash] = CaptchaCodeValue(imageCaptcha.content, Instant.now())
        return Pair(hash, imageCaptcha.image)
    }

    private fun removeAllExpiredCaptchas() {
        captchas.entries.removeIf { it.value.timestamp < Instant.now().minusSeconds(5 * 60) }
    }
}