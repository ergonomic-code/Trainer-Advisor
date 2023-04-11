package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto

interface QuestionService {
    fun createQuestion(id: Long): QuestionWithAnswersDto
    fun findQuestionWithAnswers(id: Long): QuestionWithAnswersDto
    fun deleteQuestion(id: Long): Long
    fun findQuestion(id: Long): QuestionDto
    fun updateQuestion(question: QuestionDto): Long
    fun updateQuestion(question: QuestionWithAnswersDto,): Long
}