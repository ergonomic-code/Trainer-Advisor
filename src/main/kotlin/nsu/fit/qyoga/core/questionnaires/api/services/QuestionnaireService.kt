package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface QuestionnaireService {
    fun findQuestionnaires(
        questionnaireSearchDto: QuestionnaireSearchDto,
        pageable: Pageable
    ): Page<QuestionnaireDto>

    fun createQuestionnaire(createQuestionnaireDto: CreateQuestionnaireDto): Questionnaire

    fun getQuestionnairesCount(title: String?): Long
}
