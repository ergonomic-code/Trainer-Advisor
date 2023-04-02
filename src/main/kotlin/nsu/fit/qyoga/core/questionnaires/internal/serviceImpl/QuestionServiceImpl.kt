package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionException
import nsu.fit.qyoga.core.questionnaires.api.model.Question
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionJdbcTemplateRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionRepo
import org.springframework.stereotype.Service

@Service
class QuestionServiceImpl(
    private val questionRepo: QuestionRepo,
    private val answerService: AnswerService,
    private val questionJdbcTemplateRepo: QuestionJdbcTemplateRepo
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
            answers = mutableListOf(),
            questionnaireId = id
        )
    }

    override fun findQuestionWithAnswers(id: Long): QuestionWithAnswersDto {
        return questionJdbcTemplateRepo.findQuestionWithAnswersById(id)
            ?: throw QuestionException("Выбранный вопрос не найден")
    }

    override fun deleteQuestion(id: Long): Long {
        val question: Question = questionRepo.findById(id).orElse(null)
            ?: throw QuestionException("Выбранный вопрос не найден")
        questionRepo.deleteById(id)
        return question.questionnaireId
    }

    override fun findQuestion(id: Long): QuestionDto {
        val question = questionRepo.findById(id).orElse(null)
            ?: throw QuestionException("Выбранный вопрос не найден")
        return QuestionDto(
            id = question.id,
            title = question.title,
            questionType = question.questionType,
            questionnaireId = question.questionnaireId,
            imageId = question.imageId
        )
    }

    override fun updateQuestion(question: QuestionDto): Long {
        return questionRepo.save(
            Question(
                id = question.id,
                title = question.title,
                questionType = question.questionType,
                questionnaireId = question.questionnaireId,
                imageId = question.imageId
            )
        ).id
    }

    override fun updateQuestion(
        createQuestionDto: QuestionWithAnswersDto,
        questionnaireId: Long,
        questionImageId: Long?
    ): Long {
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
        return savedQuestion.id
    }

    override fun updateQuestionTitle(id: Long, title: String) {
        questionJdbcTemplateRepo.updateQuestionTitleById(id, title)
    }

    override fun updateQuestionType(id: Long, questionType: QuestionType) {
        questionJdbcTemplateRepo.updateQuestionTypeById(id, questionType)
    }
}