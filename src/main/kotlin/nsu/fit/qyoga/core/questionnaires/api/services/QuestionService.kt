package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto


interface QuestionService {
    fun createQuestion(id: Long): CreateQuestionDto

    fun updateQuestion(
        createQuestionDto: CreateQuestionDto,
        questionnaireId: Long,
        questionImageId: Long?
    )
}