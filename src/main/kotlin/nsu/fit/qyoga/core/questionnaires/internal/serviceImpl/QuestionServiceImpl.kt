package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
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

    override fun createQuestion(id: Long): CreateQuestionDto {
        val createdQuestion = Question(
            title = "",
            questionType = QuestionType.SINGLE,
            questionnaireId = id,
            imageId = null
        )
        return CreateQuestionDto(
            id = createdQuestion.id,
            questionType = createdQuestion.questionType,
            photoId = createdQuestion.imageId,
            answers = mutableListOf()
        )
    }

    override fun updateQuestion(
        createQuestionDto: CreateQuestionDto,
        questionnaireId: Long,
        questionImageId: Long?
    ) {
        val savedQuestion = questionRepo.save(
            Question(
                title = createQuestionDto.text,
                questionType = createQuestionDto.questionType,
                questionnaireId = questionnaireId,
                imageId = questionImageId
            )
        )
        createQuestionDto.answers.map {
            answerService.createAnswer(it, savedQuestion.id, it.photoId)
        }
    }
}