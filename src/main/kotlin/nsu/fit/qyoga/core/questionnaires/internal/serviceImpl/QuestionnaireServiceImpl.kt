package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireWithQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.ImageDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import nsu.fit.qyoga.core.questionnaires.api.services.ImageService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionnaireRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.findPageByTitle
import org.springframework.data.domain.*
import org.springframework.stereotype.Service

@Service
class QuestionnaireServiceImpl(
    private val questionnaireRepo: QuestionnaireRepo,
    private val imageService: ImageService,
    private val questionService: QuestionService
    ) : QuestionnaireService {

    override fun findQuestionnaires(
        questionnaireSearchDto: QuestionnaireSearchDto,
        pageable: Pageable
    ): Page<QuestionnaireDto> {
        val page = questionnaireRepo.findPageByTitle(
            title = questionnaireSearchDto.title ?: "",
            pageable = pageable
        )
        return page.map { QuestionnaireDto(id = it.id, title = it.title) }
    }

    override fun createQuestionnaire(questionnaireWithQuestionDto: QuestionnaireWithQuestionDto): QuestionnaireDto {
        val savedQuestionnaire = questionnaireRepo.save(Questionnaire(title = questionnaireWithQuestionDto.title))
        questionnaireWithQuestionDto.questions.map {
            val questionImageId = if (it.photo != null) {
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
            questionService.createQuestion(it, savedQuestionnaire.id, questionImageId)
        }
        return QuestionnaireDto(savedQuestionnaire.id, savedQuestionnaire.title)
    }

    override fun findQuestionnaireWithQuestions(id: Long): QuestionnaireWithQuestionDto {
        return questionnaireRepo.findQuestionnaireWithQuestionsById(id)
    }
}
