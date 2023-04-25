package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerBoundsDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.errors.AnswerException
import nsu.fit.qyoga.core.questionnaires.api.model.Answer
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.internal.repository.AnswerJdbcTemplateRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.AnswerRepo
import org.springframework.stereotype.Service

@Service
class AnswerServiceImpl(
    private val answerRepo: AnswerRepo,
    private val answerJdbcTemplateRepo: AnswerJdbcTemplateRepo
) : AnswerService {
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
        return answerToAnswerDtoMapper(answer)
    }
    override fun updateAnswer(createAnswerDto: AnswerDto): AnswerDto {
        answerRepo.findById(createAnswerDto.id).orElse(null)
            ?: throw AnswerException("Выбранный ответ не найден")
        val answer = answerRepo.save(
            Answer(
                id = createAnswerDto.id,
                title = createAnswerDto.title,
                lowerBound = createAnswerDto.bounds.lowerBound,
                lowerBoundText = createAnswerDto.bounds.lowerBoundText,
                upperBound = createAnswerDto.bounds.upperBound,
                upperBoundText = createAnswerDto.bounds.upperBoundText,
                score = createAnswerDto.score,
                imageId = createAnswerDto.imageId,
                questionId = createAnswerDto.questionId
            )
        )
        return answerToAnswerDtoMapper(answer)
    }
    override fun findAnswer(id: Long): AnswerDto {
        val answer = answerRepo.findById(id).orElse(null)
            ?: throw AnswerException("Выбранный ответ не найден")
        return answerToAnswerDtoMapper(answer)
    }
    override fun deleteAnswerById(id: Long) {
        answerRepo.deleteById(id)
    }

    fun answerToAnswerDtoMapper(answer: Answer): AnswerDto {
        return AnswerDto(
            id = answer.id,
            title = answer.title,
            bounds = AnswerBoundsDto(
                lowerBound = answer.lowerBound,
                lowerBoundText = answer.lowerBoundText,
                upperBound = answer.upperBound,
                upperBoundText = answer.upperBoundText
            ),
            score = answer.score,
            imageId = answer.imageId,
            questionId = answer.questionId
        )
    }
}
