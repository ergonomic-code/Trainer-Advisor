package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto

interface QuestionService {
    fun createQuestion(id: Long): QuestionWithAnswersDto
    fun deleteQuestion(id: Long)
    fun findQuestion(id: Long): QuestionWithAnswersDto
    fun updateQuestion(question: QuestionWithAnswersDto): Long
}
