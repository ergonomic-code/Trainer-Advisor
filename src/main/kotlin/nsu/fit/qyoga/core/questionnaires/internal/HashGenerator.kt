package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.platform.lang.toHexString
import org.springframework.stereotype.Component
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class HashGenerator {

    fun generateHash(value: String, key: String): String {
        val secretKeySpec = SecretKeySpec(key.toByteArray(), "HmacSHA256")
        val mac: Mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        return mac.doFinal(value.toByteArray()).toHexString()
    }

}