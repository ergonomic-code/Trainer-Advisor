package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto

interface AnswerService {
    fun createAnswer(answerDto: AnswerDto, questionId: Long, answerImageId: Long?)
}