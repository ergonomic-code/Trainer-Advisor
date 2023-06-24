package nsu.fit.qyoga.core.questionnaires.api.services

interface QuestionnaireCompletionService {

    fun generateCompletingLink(questionnaireId: Long, clientId: Long, therapistId: Long): String
}