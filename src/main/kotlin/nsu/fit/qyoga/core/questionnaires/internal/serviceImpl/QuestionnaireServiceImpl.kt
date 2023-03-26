package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireWithQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionnaireJdbcTemplateRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionnaireRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.findPageByTitle
import org.springframework.data.domain.*
import org.springframework.stereotype.Service

@Service
class QuestionnaireServiceImpl(
    private val questionnaireRepo: QuestionnaireRepo,
    private val questionnaireJdbcTemplateRepo: QuestionnaireJdbcTemplateRepo,
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

    override fun createQuestionnaire(): Long {
        return questionnaireRepo.save(Questionnaire(title = "")).id
    }

    override fun updateQuestionnaire(createQuestionnaireDto: QuestionnaireWithQuestionDto): QuestionnaireDto {
        val savedQuestionnaire = questionnaireRepo.save(Questionnaire(title = createQuestionnaireDto.title))
        createQuestionnaireDto.questions.map {
            questionService.updateQuestion(it, savedQuestionnaire.id, it.imageId)
        }
        return QuestionnaireDto(savedQuestionnaire.id, savedQuestionnaire.title)
    }

    override fun findQuestionnaireWithQuestions(id: Long): QuestionnaireWithQuestionDto {
        return questionnaireJdbcTemplateRepo.findQuestionnaireWithQuestionsById(id)
            ?: throw QuestionnaireException("Выбранный опросник не найден")
    }
}
