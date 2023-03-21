package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.model.Answer
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.internal.repository.AnswerRepo
import org.springframework.stereotype.Service

@Service
class AnswerServiceImpl(
     private val answerRepo: AnswerRepo
) :AnswerService {

    override fun createAnswer(createAnswerDto: CreateAnswerDto, questionId: Long, answerImageId: Long?) {
        answerRepo.save(
            Answer(
                title = createAnswerDto.title,
                lowerBound = createAnswerDto.lowerBound,
                lowerBoundText = createAnswerDto.lowerBoundText,
                upperBound = createAnswerDto.upperBound,
                upperBoundText = createAnswerDto.upperBoundText,
                score = createAnswerDto.score,
                imageId = answerImageId,
                questionId = questionId
            )
        )
    }
}