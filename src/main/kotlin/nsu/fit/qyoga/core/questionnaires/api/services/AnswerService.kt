package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.model.Answer

interface AnswerService {
    fun createAnswer(id: Long): AnswerDto
    fun updateAnswer(createAnswerDto: AnswerDto, questionId: Long, answerImageId: Long?)
    fun findAnswer(id: Long): AnswerDto
    fun deleteAllByQuestionId(id: Long): List<Answer>
    fun updateAnswer(createAnswerDto: AnswerDto)
    fun deleteAnswer(id: Long): Long
}