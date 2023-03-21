package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto

interface AnswerService {
    fun createAnswer(createAnswerDto: CreateAnswerDto, questionId: Long, answerImageId: Long?)
}