package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.platform.lang.toHexString
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.HMac
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.KeyParameter
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.SHA256Digest
import org.springframework.stereotype.Component

@Component
class HashGenerator {

    fun generateHash(value: String, key: String): String {
        val hMac = HMac(SHA256Digest())
        hMac.init(KeyParameter(key.toByteArray()))
        val hmacIn = value.toByteArray()
        hMac.update(hmacIn, 0, hmacIn.size)
        val hmacOut = ByteArray(hMac.macSize)
        hMac.doFinal(hmacOut, 0)
        return hmacOut.toHexString()
    }

}