package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireWithQuestionDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface QuestionnaireService {
    fun findQuestionnaires(
        questionnaireSearchDto: QuestionnaireSearchDto,
        pageable: Pageable
    ): Page<QuestionnaireDto>

    fun createQuestionnaire(): Long

    fun updateQuestionnaire(createQuestionnaireDto: QuestionnaireWithQuestionDto): QuestionnaireDto

    fun updateQuestionnaire(questionnaire: QuestionnaireDto): QuestionnaireDto

    fun findQuestionnaireWithQuestions(id: Long): QuestionnaireWithQuestionDto
}
