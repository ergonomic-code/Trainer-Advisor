package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType

data class QuestionDto(
    val id: Long,
    val title: String?,
    val questionType: QuestionType,
    val questionnaireId: Long,
    val imageId: Long?
)