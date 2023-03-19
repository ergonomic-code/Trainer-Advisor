package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto

interface QuestionService {
    fun createQuestion(questionWithAnswersDto: QuestionWithAnswersDto, questionnaireId: Long, questionImageId: Long?)
}