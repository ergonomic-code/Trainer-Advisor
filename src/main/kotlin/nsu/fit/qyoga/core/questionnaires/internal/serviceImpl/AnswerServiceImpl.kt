package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.model.Answer
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.internal.repository.AnswerRepo
import org.springframework.stereotype.Service

@Service
class AnswerServiceImpl(
     private val answerRepo: AnswerRepo
) :AnswerService {

    override fun createAnswer(answerDto: AnswerDto, questionId: Long, answerImageId: Long?) {
        answerRepo.save(
            Answer(
                title = answerDto.title,
                lowerBound = answerDto.lowerBound,
                lowerBoundText = answerDto.lowerBoundText,
                upperBound = answerDto.upperBound,
                upperBoundText = answerDto.upperBoundText,
                score = answerDto.score,
                imageId = answerImageId,
                questionId = questionId
            )
        )
    }
}