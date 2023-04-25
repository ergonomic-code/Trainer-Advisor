package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerBoundsDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionException
import nsu.fit.qyoga.core.questionnaires.api.model.Answer
import nsu.fit.qyoga.core.questionnaires.api.model.Question
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionJdbcTemplateRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionRepo
import org.springframework.stereotype.Service

@Service
class QuestionServiceImpl(
    private val questionRepo: QuestionRepo,
    private val questionJdbcTemplateRepo: QuestionJdbcTemplateRepo
) : QuestionService {

    override fun createQuestion(id: Long): QuestionWithAnswersDto {
        val createdQuestion = questionRepo.save(
            Question(
                title = "",
                questionType = QuestionType.SINGLE,
                questionnaireId = id,
                imageId = null,
                answers = setOf(
                    Answer(
                        title = "",
                        lowerBound = null,
                        lowerBoundText = null,
                        upperBound = null,
                        upperBoundText = null,
                        score = null,
                        imageId = null
                    )
                )
            )
        )
        return QuestionWithAnswersDto(
            id = createdQuestion.id,
            title = createdQuestion.title,
            questionType = createdQuestion.questionType,
            imageId = createdQuestion.imageId,
            answers = createdQuestion.answers.map { answerToAnswerDto(it) },
            questionnaireId = id
        )
    }

    override fun deleteQuestion(id: Long) {
        questionRepo.deleteById(id)
    }

    override fun findQuestion(id: Long): QuestionWithAnswersDto {
        val question = questionRepo.findById(id).orElse(null)
            ?: throw QuestionException("Выбранный вопрос не найден")
        return QuestionWithAnswersDto(
            id = question.id,
            title = question.title,
            questionType = question.questionType,
            questionnaireId = question.questionnaireId,
            imageId = question.imageId,
            answers = question.answers.map { answerToAnswerDto(it) }
        )
    }

    override fun updateQuestion(
        question: QuestionWithAnswersDto,
    ): Long {
        return questionJdbcTemplateRepo.updateQuestion(
            Question(
                id = question.id,
                title = question.title,
                questionType = question.questionType,
                questionnaireId = question.questionnaireId,
                imageId = question.imageId,
                answers = emptySet()
            )
        )
    }

    override fun saveQuestion(question: QuestionWithAnswersDto): Long {
        val questionToSave = Question(
            id = question.id,
            title = question.title,
            questionType = question.questionType,
            questionnaireId = question.questionnaireId,
            imageId = question.imageId,
            answers = question.answers.map { answerDtoToAnswer(it) }.toSet()
        )
        val savedQuestion = questionRepo.save(
            questionToSave
        )
        return savedQuestion.id
    }

    fun answerDtoToAnswer(answerDto: AnswerDto): Answer {
        return Answer(
            title = answerDto.title,
            lowerBound = answerDto.bounds.lowerBound,
            lowerBoundText = answerDto.bounds.lowerBoundText,
            upperBound = answerDto.bounds.upperBound,
            upperBoundText = answerDto.bounds.upperBoundText,
            score = answerDto.score,
            imageId = answerDto.imageId
        )
    }

    fun answerToAnswerDto(answer: Answer): AnswerDto {
        return AnswerDto(
            answer.id,
            answer.title,
            AnswerBoundsDto(
                answer.lowerBound,
                answer.lowerBoundText,
                answer.upperBound,
                answer.upperBoundText
            ),
            answer.score,
            answer.imageId
        )
    }
}
