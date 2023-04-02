package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType

interface QuestionService {
    fun createQuestion(id: Long): QuestionWithAnswersDto

    fun findQuestionWithAnswers(id: Long): QuestionWithAnswersDto
    fun deleteQuestion(id: Long): Long

    fun findQuestion(id: Long): QuestionDto

    fun updateQuestion(question: QuestionDto): Long

    fun updateQuestionTitle(id: Long, title: String)

    fun updateQuestionType(id: Long, questionType: QuestionType)

    fun updateQuestion(
        createQuestionDto: QuestionWithAnswersDto,
        questionnaireId: Long,
        questionImageId: Long?
    ): Long
}