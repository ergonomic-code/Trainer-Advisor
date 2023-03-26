package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto

interface AnswerService {

    fun createAnswer(id: Long): AnswerDto
    fun updateAnswer(createAnswerDto: AnswerDto, questionId: Long, answerImageId: Long?)
}