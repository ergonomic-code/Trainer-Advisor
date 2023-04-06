package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.model.Answer

interface AnswerService {

    fun createAnswer(id: Long): AnswerDto
    fun updateAnswer(createAnswerDto: AnswerDto, questionId: Long, answerImageId: Long?)
    fun findAnswer(id: Long): AnswerDto
    fun deleteAllByQuestionId(id: Long): List<Answer>
    fun updateAnswerTitle(id: Long, title: String)
    fun updateAnswerLowerBound(id: Long, value: Int )
    fun updateAnswerLowerBoundTitle(id: Long, title: String)
    fun updateAnswerUpperBound(id: Long, value: Int )
    fun updateAnswerUpperBoundTitle(id: Long, title: String)
    fun updateAnswer(createAnswerDto: AnswerDto)
    fun deleteAnswer(id: Long): Long
}