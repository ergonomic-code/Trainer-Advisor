package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType
import nsu.fit.qyoga.core.questionnaires.api.model.Question
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionRepo
import org.springframework.stereotype.Service

@Service
class QuestionServiceImpl(
    private val questionRepo: QuestionRepo,
    private val answerService: AnswerService,
) : QuestionService {

    override fun createQuestion(id: Long): QuestionWithAnswersDto {
        val createdQuestion = questionRepo.save(
            Question(
                title = "",
                questionType = QuestionType.SINGLE,
                questionnaireId = id,
                imageId = null
            )
        )
        return QuestionWithAnswersDto(
            id = createdQuestion.id,
            title = createdQuestion.title,
            questionType = createdQuestion.questionType,
            imageId = createdQuestion.imageId,
            answers = mutableListOf()
        )
    }

    override fun updateQuestion(
        createQuestionDto: QuestionWithAnswersDto,
        questionnaireId: Long,
        questionImageId: Long?
    ) {
        val savedQuestion = questionRepo.save(
            Question(
                title = createQuestionDto.title,
                questionType = createQuestionDto.questionType,
                questionnaireId = questionnaireId,
                imageId = questionImageId
            )
        )
        createQuestionDto.answers.map {
            answerService.updateAnswer(it, savedQuestion.id, it.imageId)
        }
    }
}