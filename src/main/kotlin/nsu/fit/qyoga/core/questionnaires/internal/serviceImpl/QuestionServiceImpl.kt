package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.ImageDto
import nsu.fit.qyoga.core.questionnaires.api.model.Question
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.api.services.ImageService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionRepo
import org.springframework.stereotype.Service

@Service
class QuestionServiceImpl(
    private val questionRepo: QuestionRepo,
    private val imageService: ImageService,
    private val answerService: AnswerService,
) : QuestionService {

    override fun createQuestion(questionWithAnswersDto: QuestionWithAnswersDto, questionnaireId: Long, questionImageId: Long?) {
        val savedQuestion = questionRepo.save(
            Question(
                title = questionWithAnswersDto.text?: "",
                questionType = questionWithAnswersDto.questionType,
                questionnaireId = questionnaireId,
                imageId = questionImageId
            )
        )
        questionWithAnswersDto.answers.map {
            val answerImageId = if (it.photo != null) {
                imageService.uploadImage(
                    ImageDto(
                        it.photo.name,
                        it.photo.contentType ?: "application/octet-stream",
                        it.photo.size,
                        it.photo.inputStream
                    )
                )
            } else {
                null
            }
            answerService.createAnswer(it, savedQuestion.id, answerImageId)
        }

    }
}