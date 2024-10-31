package pro.qyoga.tech.captcha

import net.logicsquad.nanocaptcha.image.ImageCaptcha
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class CaptchaService {

    private val captchas = ConcurrentHashMap<UUID, CaptchaCodeValue>()

    private val captchaTtl = Duration.ofMinutes(5)

    fun generateCaptcha(): Pair<UUID, BufferedImage> {
        cleanCache(Instant.now())

        val imageCaptcha = ImageCaptcha.create()
        val hash = UUID.randomUUID()
        captchas[hash] = CaptchaCodeValue(imageCaptcha.content, Instant.now())

        return Pair(hash, imageCaptcha.image)
    }

    fun isInvalid(id: UUID, captchaCode: String): Boolean {
        return captchas.remove(id)?.captchaCode != captchaCode
    }

    private fun cleanCache(instant: Instant) {
        val minNonExpiredInstant = instant.minus(captchaTtl)
        captchas.entries.removeIf { it.value.generationInstant < minNonExpiredInstant }
    }

}

data class CaptchaCodeValue(
    val captchaCode: String,
    val generationInstant: Instant
)