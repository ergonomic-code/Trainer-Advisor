package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.platform.lang.toHexString
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.HMac
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.KeyParameter
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.SHA256Digest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CompletingLinkGenerator(
    @Value("\${qyoga.hash.secret-key}")
    private val key: String,
    @Value("\${qyoga.url.host}")
    private val host: String
) {
    fun generateCompletingLink(questionnaireId: Long, clientId: Long, therapistId: Long): String {
        val hash = generateHash("questionnaireId:${questionnaireId}clientId:${clientId}therapist:$therapistId")
        return """
            http://$host/client/questionnaire-completions?
            questionnaireId=$questionnaireId
            &clientId=$clientId
            &therapistId=$therapistId
            &hash=$hash
        """.trimIndent().replace(" ", "")
    }

    fun generateHash(value: String): String {
        val hMac = HMac(SHA256Digest())
        hMac.init(KeyParameter(key.toByteArray()))
        val hmacIn = value.toByteArray()
        hMac.update(hmacIn, 0, hmacIn.size)
        val hmacOut = ByteArray(hMac.macSize)
        hMac.doFinal(hmacOut, 0)
        return hmacOut.toHexString()
    }
}