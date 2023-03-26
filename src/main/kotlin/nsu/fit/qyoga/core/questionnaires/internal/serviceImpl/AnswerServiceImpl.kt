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
    override fun createAnswer(id: Long): AnswerDto {
        val answer = answerRepo.save(
            Answer(
                title = "",
                lowerBound = null,
                lowerBoundText = null,
                upperBound = null,
                upperBoundText = null,
                score = null,
                imageId = null,
                questionId = id
            )
        )
        return AnswerDto(
            id = answer.id,
            title = answer.title,
            lowerBound = answer.lowerBound,
            lowerBoundText = answer.lowerBoundText,
            upperBound = answer.upperBound,
            upperBoundText = answer.upperBoundText,
            score = answer.score,
            imageId = answer.imageId,
        )
    }

    override fun updateAnswer(createAnswerDto: AnswerDto, questionId: Long, answerImageId: Long?) {
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