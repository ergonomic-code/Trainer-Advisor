package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto

interface QuestionService {

    fun createQuestion(createQuestionDto: CreateQuestionDto, questionnaireId: Long, imageId: Long?)
    fun loadQuestionsWithAnswers(id: Long): List<CreateQuestionDto>
}