package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireCompletionService
import nsu.fit.qyoga.core.questionnaires.internal.HashGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class QuestionnaireCompletionServiceImpl(
    @Value("\${qyoga.hash.secret-key}")
    private val key: String,
    @Value("\${qyoga.url.host}")
    private val host: String,
    private val hashGenerator: HashGenerator
) : QuestionnaireCompletionService {
    override fun generateCompletingLink(questionnaireId: Long, clientId: Long, therapistId: Long): String {
        val hash = hashGenerator.generateHash(
            "questionnaireId:${questionnaireId}clientId:${clientId}therapist:$therapistId",
            key
        )
        return """
            $host/client/questionnaire-completions?
            questionnaireId=$questionnaireId
            &clientId=$clientId
            &therapistId=$therapistId
            &hash=$hash
        """.trimIndent().replace(" ", "")
    }
}