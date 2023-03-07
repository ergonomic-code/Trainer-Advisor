package nsu.fit.qyoga.core.questionnaires.api

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.utils.Page

interface QuestionnaireService {
    fun findQuestionnaires(title: String?, page: Page): List<QuestionnaireDto>
    fun getQuestionnairesCount(title: String?): Long
}
