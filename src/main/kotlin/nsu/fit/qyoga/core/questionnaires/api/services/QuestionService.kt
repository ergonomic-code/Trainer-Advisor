package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionDto
import org.springframework.stereotype.Service

interface QuestionService {

    fun createQuestion(questionDto: QuestionDto, questionnaireId: Long, imageId: Long)
}