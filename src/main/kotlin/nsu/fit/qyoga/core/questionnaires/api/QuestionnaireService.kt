package nsu.fit.qyoga.core.questionnaires.api

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface QuestionnaireService {
    fun findQuestionnaires(questionnaireSearchDto: QuestionnaireSearchDto, pageable: Pageable): Page<QuestionnaireDto>
    fun getQuestionnairesCount(title: String?): Long
}
