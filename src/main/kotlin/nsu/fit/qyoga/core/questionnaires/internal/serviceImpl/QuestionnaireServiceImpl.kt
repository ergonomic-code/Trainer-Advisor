package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.ImageDto
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import nsu.fit.qyoga.core.questionnaires.api.services.ImagesService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionnaireRepo
import org.springframework.data.domain.*
import org.springframework.stereotype.Service

@Service
class QuestionnaireServiceImpl(
    private val questionnaireRepo: QuestionnaireRepo,
    private val questionService: QuestionService,
    private val imagesService: ImagesService
) : QuestionnaireService {

    override fun findQuestionnaires(
        questionnaireSearchDto: QuestionnaireSearchDto,
        pageable: Pageable
    ): Page<QuestionnaireDto> {
        val sort = if (questionnaireSearchDto.orderType == "DESK") {
            Sort.by("title").descending()
        } else {
            Sort.by("title").ascending()
        }
        val pageRequest = PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
        val title = questionnaireSearchDto.title
        val entities = if (title == null) {
            questionnaireRepo.findAll(pageRequest)
        } else {
            questionnaireRepo.findAllByTitleContaining(title, pageRequest)
        }
        val count = getQuestionnairesCount(questionnaireSearchDto.title)
        return PageImpl(entities.map { QuestionnaireDto(id = it.id, title = it.title) }.toList(), pageRequest, count)
    }

    override fun createQuestionnaire(createQuestionnaireDto: CreateQuestionnaireDto): Questionnaire {
        val questionnaire = Questionnaire(
            title = createQuestionnaireDto.title?: "unnamed"
        )
        val savedQuestionnaire = questionnaireRepo.save(questionnaire)
        createQuestionnaireDto.questions.map {
            val imageId = if (it.photo != null) {
                imagesService.uploadImage(
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
            questionService.createQuestion(it, savedQuestionnaire.id, imageId)
        }
        return savedQuestionnaire
    }

    override fun getQuestionnairesCount(title: String?): Long {
        return if (title != null) {
            questionnaireRepo.countAllByTitleContaining(title)
        } else {
            questionnaireRepo.count()
        }
    }

    override fun loadQuestionnairesWithQuestions(id: Long): CreateQuestionnaireDto {
        val questionnaire = questionnaireRepo.findById(id)
        val questions = questionService.loadQuestionsWithAnswers(id)
        return CreateQuestionnaireDto(
            title = questionnaire.get().title,
            questions = emptyList()
        )
    }
}
