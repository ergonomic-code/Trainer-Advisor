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

    override fun deleteQuestion(id: Long) {
        questionRepo.deleteById(id)
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
        questionRepo.findById(question.id).orElse(null)
            ?: throw QuestionException("Выбранный вопрос не найден")
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
        question: QuestionWithAnswersDto,
    ): Long {
        val savedQuestion = questionRepo.save(
            Question(
                id = question.id,
                title = question.title,
                questionType = question.questionType,
                questionnaireId = question.questionnaireId,
                imageId = question.imageId
            )
        )
        question.answers.map {
            it.questionId = savedQuestion.id
            answerService.updateAnswer(it)
        }
        return savedQuestion.id
    }
}
